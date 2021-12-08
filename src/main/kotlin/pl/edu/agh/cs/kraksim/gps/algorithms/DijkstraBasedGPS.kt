package pl.edu.agh.cs.kraksim.gps.algorithms

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.addToFront
import pl.edu.agh.cs.kraksim.common.popMinBy
import pl.edu.agh.cs.kraksim.common.toArrayList
import pl.edu.agh.cs.kraksim.core.state.Gateway
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.gps.GPSType

@Component
class DijkstraBasedGPS {

    fun calculate(
        source: Gateway,
        target: Gateway,
        state: SimulationState,
        getRoadWeight: (Road) -> Double,
        gpsType: GPSType
    ): GPS {
        require(source != target) { "GPS route target and source cannot be the same gateway (name=${source.name})" }
        val route = calculateDijkstra(state, source, target, getRoadWeight)
        if (route.isEmpty()) throw IllegalStateException("Target gateway (name=${target.name}) cannot be reached from source (name=${source.name})")
        return GPS(route, gpsType)
    }

    private fun calculateDijkstra(
        state: SimulationState,
        source: Gateway,
        target: Gateway,
        getRoadWeight: (Road) -> Double
    ): ArrayList<Road> {
        val directRouteToTarget = checkIfTargetIsAdjacentToSource(source, target)
        if (directRouteToTarget.isNotEmpty()) {
            return directRouteToTarget
        }

        val (notReachedNodes, weightFromSource, pathRecovery) = initialize(state, source, getRoadWeight)

        val fastestRoadLeadingToTarget =
            calculateShortestPath(notReachedNodes, weightFromSource, pathRecovery, target, getRoadWeight)
        return parseRecoveryToRoute(fastestRoadLeadingToTarget, pathRecovery)
    }

    /**
     * Due to modification that Dijkstra is calculated from roads and not nodes
     * in case of target being directly reachable by one road from source path recovery yields no results
     */
    private fun checkIfTargetIsAdjacentToSource(
        source: Gateway,
        target: Gateway
    ) = source.startingRoads.values.filter { it.end == target }.toArrayList()

    private fun initialize(
        state: SimulationState,
        source: Gateway,
        getRoadWeight: (Road) -> Double
    ): Triple<HashSet<Road>, HashMap<Road, Double>, HashMap<Road, Road>> {
        val notReachedNodes = HashSet<Road>()
        val weightFromSource = HashMap<Road, Double>()
        val pathRecovery = HashMap<Road, Road>()

        for (node in state.roads.values) {
            weightFromSource[node] = Double.MAX_VALUE
            notReachedNodes.add(node)
        }

        for (node in source.startingRoads.values) {
            weightFromSource[node] = getRoadWeight(node)
        }

        return Triple(notReachedNodes, weightFromSource, pathRecovery)
    }

    private fun calculateShortestPath(
        notReachedNodes: HashSet<Road>,
        weightFromSource: HashMap<Road, Double>,
        pathRecovery: HashMap<Road, Road>,
        target: Gateway,
        getRoadWeight: (Road) -> Double
    ): Road? {
        while (notReachedNodes.isNotEmpty()) {
            val currentRoad = notReachedNodes.popMinBy { weightFromSource[it]!! }

            val end = currentRoad.end
            if (end == target) {
                return currentRoad
            }
            if (end is Intersection) {
                for (roadFromCurrentNode in end.getPossibleRoads(currentRoad)) {
                    val neighbourNodeWeightFromSource =
                        weightFromSource[currentRoad]!! + getRoadWeight(roadFromCurrentNode)

                    if (neighbourNodeWeightFromSource < weightFromSource[roadFromCurrentNode]!!) {
                        weightFromSource[roadFromCurrentNode] = neighbourNodeWeightFromSource
                        pathRecovery[roadFromCurrentNode] = currentRoad
                    }
                }
            }
        }
        return null
    }

    private fun parseRecoveryToRoute(
        target: Road?,
        pathRecovery: HashMap<Road, Road>
    ): ArrayList<Road> {
        if (pathRecovery[target] == null) return ArrayList()
        var find: Road? = target
        val route = ArrayList<Road>()
        while (find != null) {
            route.addToFront(find)
            find = pathRecovery[find]
        }
        return route
    }
}
