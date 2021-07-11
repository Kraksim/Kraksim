package pl.edu.agh.cs.kraksim.gps

import pl.edu.agh.cs.kraksim.common.addToFront
import pl.edu.agh.cs.kraksim.common.popMinBy
import pl.edu.agh.cs.kraksim.core.state.Gateway
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.core.state.SimulationState

abstract class DijkstraBasedGPS(
    source: Gateway,
    target: Gateway,
    state: SimulationState
) : GPS {

    override val route = ArrayList<Road>()

    abstract fun getRoadWeight(node: Road): Double

    init {
        if (source != target) {
            calculateDijkstra(state, source, target)
        }
    }

    private fun calculateDijkstra(state: SimulationState, source: Gateway, target: Gateway) {
        val (notReachedNodes, weightFromSource, pathRecovery) = initialize(state, source)

        val fastestRoadLeadingToTarget = calculateShortestPath(notReachedNodes, weightFromSource, pathRecovery, target)
        parseRecoveryToRoute(fastestRoadLeadingToTarget, pathRecovery)
    }

    private fun initialize(
        state: SimulationState,
        source: Gateway
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
        target: Gateway
    ): Road? {
        while (notReachedNodes.isNotEmpty()) {
            val currentRoad = notReachedNodes.popMinBy { weightFromSource[it]!! }

            val end = currentRoad.end()
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

    private fun parseRecoveryToRoute(target: Road?, pathRecovery: HashMap<Road, Road>) {
        var find: Road? = target
        while (find != null) {
            route.addToFront(find)
            find = pathRecovery[find]
        }
    }

    override fun getNext(): Road =
        route.first()

    override fun popNext(): Road =
        route.removeAt(0)
}
