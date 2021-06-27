package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.nagelCore.state.*
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor

class OneLaneNagelStateBuilder(
    private val intersectionsIds: IntRange,
    gatewaysIds: IntRange
) {
    private val connections: Map<Long, ArrayList<ConnectionData>>
    private val directionRelations: Map<Long, ArrayList<TurningLaneDirection>>

    init {
        if (intersectionsIds.intersect(gatewaysIds).isNotEmpty()) {
            throw IllegalArgumentException("For easiness of implementation ids of intersections and gateways should be different")
        }

        connections = (intersectionsIds + gatewaysIds).associate { id -> id.toLong() to ArrayList() }
        directionRelations = intersectionsIds.associate { id -> id.toLong() to ArrayList() }
    }

    fun connect(sourceId: Long, destinationId: Long, length: Int, roadId: Long): OneLaneNagelStateBuilder {
        val road = createRoad(roadId, length)
        connections[sourceId]?.add(ConnectionData(destinationId, road))
        return this
    }

    fun turnDirection(intersectionId: Long, sourceRoadId: Long, destinationRoadId: Long): OneLaneNagelStateBuilder {
        val direction = TurningLaneDirection(sourceRoadId, destinationRoadId)
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
                .associateWith { TrafficLightPhase(Int.MAX_VALUE, LightColor.RED) }
        )
    }

    private fun prepareDirections(
        id: Long,
        endingRoads: List<NagelRoad>,
        startingRoads: List<NagelRoad>
    ) = directionRelations[id]?.map { (sourceRoadId, destinationRoadId) ->
        val from = endingRoads.find { it.id == sourceRoadId }
            ?: throw IllegalArgumentException("Bad configuration, no ending road of id: $sourceRoadId")
        val to = startingRoads.find { it.id == destinationRoadId }
            ?: throw IllegalArgumentException("Bad configuration, no starting road of id: $destinationRoadId")

        NagelIntersectionTurningLaneDirection(from.lanes[0], to)
    }?.toSet() ?: emptySet()

    private fun createRoad(id: Long, length: Int): NagelRoad {
        val road = NagelRoad(id, length)
        road.addLane(
            laneId = id,
            indexFromLeft = 0,
            physicalStartingPoint = 0,
            physicalEndingPoint = length
        )
        return road
    }

    private data class ConnectionData(
        val destinationNodeId: Long,
        val roadConnecting: NagelRoad
    )

    private data class TurningLaneDirection(
        val sourceRoadId: Long,
        val destinationRoadId: Long
    )
}

/*
    G(0) ---> G(1)
 */
fun getOneRoadSimulationState(
    roadLength: Int = 18
): NagelSimulationState {
    return OneLaneNagelStateBuilder(IntRange.EMPTY, 0..1)
        .connect(sourceId = 0, destinationId = 1, length = roadLength, roadId = 0)
        .build(1)
}

/*
    G(1) ---> I(0) ---> G(2)
 */
fun getTwoRoadConnectedWithIntersectionSimulationState(
    firstRoadLength: Int = 18,
    secondRoadLength: Int = 18,
    trafficLightColor: LightColor = LightColor.GREEN
): NagelSimulationState {

    val state = OneLaneNagelStateBuilder(0..0, 1..2)
        .connect(sourceId = 1, destinationId = 0, length = firstRoadLength, roadId = 0)
        .connect(sourceId = 0, destinationId = 2, length = secondRoadLength, roadId = 1)
        .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 1)
        .build(1)

    state.intersection(0).phases.forEach { (_, phase) ->
        phase.state = trafficLightColor
    }
    return state
}

/*
    G(1) ---> I(0) < --- G(2)
               |
               |
               |
               V
              G(3)
 */
fun getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState(
    firstRoadLength: Int = 18,
    secondRoadLength: Int = 18,
    destinationRoadLength: Int = 18
): NagelSimulationState {

    val state = OneLaneNagelStateBuilder(0..0, 1..3)
        .connect(sourceId = 1, destinationId = 0, length = firstRoadLength, roadId = 0)
        .connect(sourceId = 2, destinationId = 0, length = secondRoadLength, roadId = 1)
        .connect(sourceId = 0, destinationId = 3, length = destinationRoadLength, roadId = 2)
        .turnDirection(intersectionId = 0, sourceRoadId = 0, destinationRoadId = 2)
        .turnDirection(intersectionId = 0, sourceRoadId = 1, destinationRoadId = 2)
        .build()

    state.intersection(0).phases.forEach { (_, phase) ->
        phase.state = LightColor.GREEN
    }
    return state
}
