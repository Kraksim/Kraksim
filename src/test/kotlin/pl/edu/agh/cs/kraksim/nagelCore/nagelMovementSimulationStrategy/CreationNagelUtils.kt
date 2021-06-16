package pl.edu.agh.cs.kraksim.nagelCore.nagelMovementSimulationStrategy

import pl.edu.agh.cs.kraksim.nagelCore.state.*
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase.LightColor

fun NagelSimulationState.getLine(road: Int = 0, lane: Int = 0): NagelLane = roads[road].lanes[lane]


fun getOneRoadSimulationState(
    roadLength: Int = 18
): NagelSimulationState {
    val road = NagelRoad(1, roadLength)
    road.addLane(
        laneId = 1,
        indexFromLeft = 0,
        physicalStartingPoint = 0,
        physicalEndingPoint = roadLength
    )

    val fromGateway = NagelGateway(
        id = 1,
        startingRoads = listOf(road),
        endingRoads = emptyList()
    )

    val toGateway = NagelGateway(
        id = 2,
        startingRoads = emptyList(),
        endingRoads = listOf(road)
    )

    road.setEnd(toGateway)

    return NagelSimulationState(
        id = 1,
        turn = 1,
        gateways = listOf(fromGateway, toGateway),
        roads = listOf(road),
        intersections = emptyList()
    )
}

fun getTwoRoadConnectedWithIntersectionSimulationState(
    firstRoadLength: Int = 18,
    secondRoadLength: Int = 18,
    trafficLightColor: LightColor = LightColor.GREEN
): NagelSimulationState {
    val road1 = NagelRoad(1, firstRoadLength)
    road1.addLane(
        laneId = 1,
        indexFromLeft = 0,
        physicalStartingPoint = 0,
        physicalEndingPoint = firstRoadLength
    )

    val road2 = NagelRoad(2, secondRoadLength)
    road2.addLane(
        laneId = 2,
        indexFromLeft = 0,
        physicalStartingPoint = 0,
        physicalEndingPoint = secondRoadLength
    )

    val directions = setOf(
        NagelIntersectionTurningLaneDirection(road1.lanes[0], road2),
    )

    val fromGateway = NagelGateway(
        id = 1,
        startingRoads = listOf(road1),
        endingRoads = emptyList()
    )

    val intersection = NagelIntersection(
        id = 1,
        endingRoads = listOf(road1),
        startingRoads = listOf(road2),
        directions = directions,
        phases = mapOf(
            road1.lanes[0] to TrafficLightPhase(Int.MAX_VALUE, trafficLightColor)
        )
    )

    val toGateway = NagelGateway(
        id = 2,
        startingRoads = emptyList(),
        endingRoads = listOf(road2)
    )

    road1.setEnd(intersection)
    road2.setEnd(toGateway)

    return NagelSimulationState(
        id = 2,
        turn = 1,
        gateways = listOf(fromGateway, toGateway),
        roads = listOf(road1, road2),
        intersections = listOf(intersection)
    )
}

fun getTwoRoadMeetingInIntersectionLeadingToThirdRoadSimulationState(
    firstRoadLength: Int = 18,
    secondRoadLength: Int = 18,
    destinationRoadLength: Int = 18,
    firstTrafficLightColor: LightColor = LightColor.GREEN,
    secondTrafficLightColor: LightColor = LightColor.GREEN
): NagelSimulationState {
    val road1 = NagelRoad(1, firstRoadLength)
    road1.addLane(
        laneId = 1,
        indexFromLeft = 0,
        physicalStartingPoint = 0,
        physicalEndingPoint = firstRoadLength
    )

    val road2 = NagelRoad(2, secondRoadLength)
    road2.addLane(
        laneId = 2,
        indexFromLeft = 0,
        physicalStartingPoint = 0,
        physicalEndingPoint = secondRoadLength
    )

    val road3 = NagelRoad(3, destinationRoadLength)
    road3.addLane(
        laneId = 3,
        indexFromLeft = 0,
        physicalStartingPoint = 0,
        physicalEndingPoint = destinationRoadLength
    )

    val directions = setOf(
        NagelIntersectionTurningLaneDirection(road1.lanes[0], road3),
        NagelIntersectionTurningLaneDirection(road2.lanes[0], road3)
    )

    val road1StartGateway = NagelGateway(
        id = 1,
        startingRoads = listOf(road1),
        endingRoads = emptyList()
    )

    val road2StartGateway = NagelGateway(
        id = 2,
        startingRoads = listOf(road2),
        endingRoads = emptyList()
    )

    val intersection = NagelIntersection(
        id = 1,
        endingRoads = listOf(road1, road2),
        startingRoads = listOf(road3),
        directions = directions,
        phases = mapOf(
            road1.lanes[0] to TrafficLightPhase(Int.MAX_VALUE, firstTrafficLightColor),
            road2.lanes[0] to TrafficLightPhase(Int.MAX_VALUE, secondTrafficLightColor)
        )
    )

    val toGateway = NagelGateway(
        id = 3,
        startingRoads = emptyList(),
        endingRoads = listOf(road3)
    )

    road1.setEnd(intersection)
    road2.setEnd(intersection)
    road3.setEnd(toGateway)

    return NagelSimulationState(
        id = 3,
        turn = 1,
        gateways = listOf(road1StartGateway, road2StartGateway, toGateway),
        roads = listOf(road1, road2, road3),
        intersections = listOf(intersection)
    )
}
