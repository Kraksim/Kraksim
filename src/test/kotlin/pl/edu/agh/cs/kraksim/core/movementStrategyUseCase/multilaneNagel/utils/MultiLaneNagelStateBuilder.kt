package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.multilaneNagel.utils

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.RoadNodeId
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelGateway
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelIntersection
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelRoad
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.core.state.IntersectionTurningLaneDirection
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase

class MultiLaneNagelStateBuilder(
    private val intersectionsIds: IntRange,
    gatewaysIds: IntRange
) {

    private val connections: Map<RoadNodeId, ArrayList<ConnectionData>>
    private val directionRelations: Map<IntersectionId, ArrayList<TurningLaneDirection>>

    init {
        require(intersectionsIds.intersect(gatewaysIds).isEmpty()) {
            "For easiness of implementation ids of intersections and gateways should be different"
        }

        connections = (intersectionsIds + gatewaysIds).associate { id -> id.toLong() to ArrayList() }
        directionRelations = intersectionsIds.associate { id -> id.toLong() to ArrayList() }
    }

    fun connect(sourceId: Long, destinationId: Long, road: NagelRoad): MultiLaneNagelStateBuilder {
        connections[sourceId]?.add(ConnectionData(destinationId, road))

        return this
    }

    fun turnDirection(
        intersectionId: Long,
        sourceRoadId: Long,
        sourceLaneId: Long,
        destinationRoadId: Long
    ): MultiLaneNagelStateBuilder {
        val direction = TurningLaneDirection(sourceRoadId, sourceLaneId, destinationRoadId)
        directionRelations[intersectionId]?.add(direction)
        return this
    }

    fun build(stateId: Long = 1): NagelSimulationState {

        val gateways = ArrayList<NagelGateway>()
        val intersections = ArrayList<NagelIntersection>()

        connections.forEach { (id, data) ->
            val startingRoads = data.map { it.roadConnecting }
            val endingRoads = connections.values.mapNotNull { connections ->
                connections.find { it.destinationNodeId == id }?.roadConnecting
            }

            if (id in intersectionsIds) {
                val intersection = createIntersection(id, endingRoads, startingRoads)
                intersections.add(intersection)
            } else {
                val gateway = NagelGateway(
                    id = id,
                    startingRoads = startingRoads,
                    endingRoads = endingRoads
                )
                gateways.add(gateway)
            }
        }

        return NagelSimulationState(
            id = stateId,
            turn = 1,
            gateways = gateways,
            roads = connections.values.flatten().map { it.roadConnecting },
            intersections = intersections
        )
    }

    private fun createIntersection(
        id: Long,
        endingRoads: List<NagelRoad>,
        startingRoads: List<NagelRoad>
    ): NagelIntersection {
        val directions = prepareDirections(id, endingRoads, startingRoads)

        return NagelIntersection(
            id = id,
            endingRoads = endingRoads,
            startingRoads = startingRoads,
            directions = directions,
            phases = endingRoads.flatMap { it.lanes }
                .associate { lane -> lane.id to TrafficLightPhase(Int.MAX_VALUE, TrafficLightPhase.LightColor.RED) }
        )
    }

    private fun prepareDirections(
        id: Long,
        endingRoads: List<NagelRoad>,
        startingRoads: List<NagelRoad>
    ) = directionRelations[id]?.map { (sourceRoadId, sourceLaneId, destinationRoadId) ->
        requireNotNull(startingRoads.find { it.id == destinationRoadId }) {
            "Bad configuration, no starting road of id: $destinationRoadId"
        }
        val roadFrom = requireNotNull(endingRoads.find { it.id == sourceRoadId }) {
            "Bad configuration, no ending road of id: $sourceRoadId"
        }

        val laneFrom = requireNotNull(roadFrom.lanes.find { it.id == sourceLaneId }) {
            "Bad configuration, no lane of id: $sourceLaneId in road with id: $sourceRoadId"
        }

        IntersectionTurningLaneDirection(laneFrom.id, destinationRoadId)
    } ?: emptyList()

    private data class ConnectionData(
        val destinationNodeId: Long,
        val roadConnecting: NagelRoad
    )

    private data class TurningLaneDirection(
        val sourceRoadId: Long,
        val sourceLaneId: Long,
        val destinationRoadId: Long
    )
}
