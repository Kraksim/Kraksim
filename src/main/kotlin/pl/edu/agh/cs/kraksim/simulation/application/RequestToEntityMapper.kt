package pl.edu.agh.cs.kraksim.simulation.application

import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.exception.ConfigurationErrorService
import pl.edu.agh.cs.kraksim.common.exception.InvalidMapConfigurationException
import pl.edu.agh.cs.kraksim.common.getRepeatsBy
import pl.edu.agh.cs.kraksim.simulation.domain.*
import pl.edu.agh.cs.kraksim.simulation.web.request.*
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase
import pl.edu.agh.cs.kraksim.trafficLight.web.request.CreateLightPhaseStrategyRequest
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.*
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType.Companion.isSingleLaneBased
import pl.edu.agh.cs.kraksim.trafficState.domain.request.CreateGatewayStateRequest
import pl.edu.agh.cs.kraksim.trafficState.domain.request.CreateInitialSimulationStateRequest

@Component
class RequestToEntityMapper(
    val errorService: ConfigurationErrorService
) {
    private val log = LogManager.getLogger()

    fun createSimulation(request: CreateSimulationRequest, mapEntity: MapEntity): SimulationEntity {
        val movementSimulationStrategyEntity = createMovementSimulationStrategy(request.movementSimulationStrategy)
        val lightPhaseStrategyEntities = createLightPhaseStrategies(request.lightPhaseStrategies)
        val stateType = when (request.simulationType) {
            SimulationType.NAGEL_CORE -> StateType.NAGEL_SCHRECKENBERG
        }

        return SimulationEntity(
            name = request.name,
            mapEntity = mapEntity,
            simulationStateEntities = arrayListOf(
                createInitialSimulationState(
                    request.initialState,
                    stateType,
                    mapEntity.roadNodes
                )
            ),
            movementSimulationStrategy = movementSimulationStrategyEntity,
            simulationType = request.simulationType,
            expectedVelocity = request.expectedVelocity,
            lightPhaseStrategies = lightPhaseStrategyEntities,
            statisticsEntities = ArrayList()
        )
    }

    private fun createMovementSimulationStrategy(request: CreateMovementSimulationStrategyRequest): MovementSimulationStrategyEntity {
        return MovementSimulationStrategyEntity(
            type = request.type,
            randomProvider = request.randomProvider,
            slowDownProbability = request.slowDownProbability,
            maxVelocity = request.maxVelocity,
            threshold = request.threshold,
            accelerationDelayProbability = request.accelerationDelayProbability,
            breakLightReactionProbability = request.breakLightReactionProbability
        )
    }

    private fun createLightPhaseStrategies(requests: List<CreateLightPhaseStrategyRequest>): List<LightPhaseStrategyEntity> {
        return requests.map { request ->
            LightPhaseStrategyEntity(
                algorithm = request.algorithm,
                intersections = request.intersections,
                turnLength = request.turnLength,
                phiFactor = request.phiFactor,
                minPhaseLength = request.minPhaseLength
            )
        }
    }

    private fun createInitialSimulationState(
        request: CreateInitialSimulationStateRequest,
        stateType: StateType,
        roadNodes: List<RoadNodeEntity>
    ): SimulationStateEntity {
        return SimulationStateEntity(
            turn = 0,
            carsOnMap = ArrayList(),
            stateType = stateType,
            gatewaysStates = createGatewayStates(request.gatewaysStates),
            trafficLights = createTrafficLights(roadNodes)
        )
    }

    private fun createGatewayStates(requests: List<CreateGatewayStateRequest>): List<GatewayStateEntity> {
        return requests.map {
            GatewayStateEntity(
                gatewayId = it.gatewayId,
                collectedCars = ArrayList(),
                generators = it.generators.map { generator ->
                    GeneratorEntity(
                        lastCarReleasedTurnsAgo = 0,
                        releaseDelay = generator.releaseDelay,
                        carsToRelease = generator.carsToRelease,
                        targetGatewayId = generator.targetGatewayId,
                        gpsType = generator.gpsType
                    )
                }
            )
        }
    }

    private fun createTrafficLights(roadNodes: List<RoadNodeEntity>): List<TrafficLightEntity> {
        return roadNodes.filter { it.type == RoadNodeType.INTERSECTION }
            .map { intersectionEntity ->
                TrafficLightEntity(
                    intersectionId = intersectionEntity.id,
                    phases = intersectionEntity.endingRoads.flatMap { it.lanes }
                        .map { lane ->
                            PhaseEntity(
                                phaseTime = 0,
                                laneId = lane.id,
                                state = TrafficLightPhase.LightColor.RED,
                                period = null,
                            )
                        }
                )
            }
    }

    fun createMap(createMapRequest: CreateMapRequest): MapEntity {
        validateCreateMap(createMapRequest)
        val lanes: Map<Long, LaneEntity> = createMapRequest.roads
            .flatMap { road -> road.lanes.map { lane -> Pair(lane, road.name) } }.associate {
                it.first.id to createLane(it.first, it.second)
            }
        val roads: Map<Long, RoadEntity> = createMapRequest.roads.associate {
            it.id to createRoad(it, lanes)
        }
        val roadNodes = createMapRequest.roadNodes.map {
            createRoadNode(it, roads, lanes)
        }
        return MapEntity(
            type = createMapRequest.type,
            roadNodes = roadNodes,
            roads = roads.values.toList(),
            name = createMapRequest.name,

            description = createMapRequest.description,

            compatibleWith = createMapRequest.compatibleWith
        )
    }

    fun createLane(createLaneRequest: CreateLaneRequest, roadName: String) = LaneEntity(
        startingPoint = createLaneRequest.startingPoint,
        endingPoint = createLaneRequest.endingPoint,
        indexFromLeft = createLaneRequest.indexFromLeft,
        name = roadName + createLaneRequest.indexFromLeft,
    )

    fun createRoad(createRoadRequest: CreateRoadRequest, lanes: Map<Long, LaneEntity>) = RoadEntity(
        createRoadRequest.length,
        createRoadRequest.lanes.map { laneDTO ->
            lanes[laneDTO.id]!!
        },
        createRoadRequest.name
    )

    fun createRoadNode(
        createRoadNodeRequest: CreateRoadNodeRequest,
        roads: Map<Long, RoadEntity>,
        lanes: Map<Long, LaneEntity>
    ) =
        RoadNodeEntity(
            type = createRoadNodeRequest.type,
            position = PositionEntity(x = createRoadNodeRequest.position.x, y = createRoadNodeRequest.position.y),
            endingRoads = createRoadNodeRequest.endingRoadsIds.map { id ->
                roads[id] ?: throw InvalidMapConfigurationException(listOf("Road with id=$id doesn't exist"))
            },
            startingRoads = createRoadNodeRequest.startingRoadsIds.map { id ->
                roads[id] ?: throw InvalidMapConfigurationException(listOf("Road with id=$id doesn't exist"))
            },
            turnDirections = getTurnDirections(createRoadNodeRequest, lanes, roads),
            name = createRoadNodeRequest.name
        )

    private fun validateCreateMap(createMapRequest: CreateMapRequest) {
        validateCompatibleStrategies(createMapRequest)
        validateRoadNodes(createMapRequest.roadNodes)
        validateRoads(createMapRequest.roads)
    }

    private fun validateCompatibleStrategies(request: CreateMapRequest) {
        if (request.compatibleWith.any(::isSingleLaneBased)) {
            request.roads.flatMap { road -> road.lanes.map { lane -> road to lane } }
                .forEach { (road, lane) ->
                    if (lane.startingPoint != 0 || lane.endingPoint != road.length) {
                        errorService.add("Road name='${road.name}' has lane id='${lane.id}' incompatible with defined strategies - single based strategies must have lanes stretching on full road length")
                    }
                }
        }
    }

    private fun validateRoadNodes(roadNodes: List<CreateRoadNodeRequest>) {
        if (roadNodes.isEmpty())
            errorService.add("Cannot create map without any road nodes")
        if (roadNodes.none { it.type == RoadNodeType.GATEWAY })
            errorService.add("Map has to contain at least one gateway")

        roadNodes.forEach { validateRoadNode(it) }
        roadNodes.getRepeatsBy { it.name }
            .forEach { errorService.add("Bad intersection configuration name='${it.key}' - road node names have to be unique") }
    }

    private fun validateRoads(roads: List<CreateRoadRequest>) {
        if (roads.isEmpty())
            errorService.add("Cannot create map without any roads")

        val repeatedRoadIds = roads.getRepeatsBy { it.id }
        if (repeatedRoadIds.isNotEmpty()) {
            errorService.add("Road ids must be unique (${repeatedRoadIds.keys})")
        }

        val repeatedRoadNames = roads.getRepeatsBy { it.name }
        if (repeatedRoadNames.isNotEmpty()) {
            errorService.add("Road names must be unique (${repeatedRoadNames.keys})")
        }

        val lanes = roads.flatMap { it.lanes }
        val repeatedLaneIds = lanes.getRepeatsBy { it.id }
        if (repeatedLaneIds.isNotEmpty()) {
            errorService.add("Lane ids must be unique (${repeatedLaneIds.keys})")
        }

        roads.forEach { validateLanes(it) }
    }

    private fun validateLanes(road: CreateRoadRequest) {
        val errorPrefix = "Road id=${road.id} has incorrect lane configuration -"

        val minIndexFromLeft = road.lanes.minOf { it.indexFromLeft }
        val maxIndexFromLeft = road.lanes.maxOf { it.indexFromLeft }

        errorService.addAll(
            road.lanes.filter { it.startingPoint < 0 || it.endingPoint > road.length }
                .map { "$errorPrefix lane id=${it.id} has incorrect starting or ending point" }
        )

        if (minIndexFromLeft != 0)
            errorService.add("$errorPrefix lane index from left has to start from 0")

        if (maxIndexFromLeft != road.lanes.size - 1)
            errorService.add("$errorPrefix lane index from left have to be in sequence from 0 to number of lanes - 1")

        validateIfRoadCanBeDrivenThrough(road, errorPrefix)
    }

    private fun validateIfRoadCanBeDrivenThrough(road: CreateRoadRequest, errorPrefix: String) {
        val startingLanes = road.lanes.filter { it.startingPoint == 0 }
        if (startingLanes.isEmpty())
            errorService.add("$errorPrefix no lane with starting point = 0")

        val canBeDrivenThrough =
            startingLanes.all {
                tryReachEndRecc(
                    it,
                    changeLanePosition = 0,
                    road.lanes,
                    road.length,
                    visitedLanes = emptyList()
                )
            } // we must assume that from every lane starting where road start we can reach end
        // because while simulating we can't distinguish which starting lane will lead to road end
        if (!canBeDrivenThrough)
            errorService.add("$errorPrefix can't reach end of the road with this lane configuration")
    }

    /**
     * we keep changeLanePosition to invalidate this shape of lanes:
     *          |
     *  | | |   |
     *  |   | | |
     *  |
     */
    private fun tryReachEndRecc(
        it: CreateLaneRequest,
        changeLanePosition: Int,
        lanes: List<CreateLaneRequest>,
        length: Int,
        visitedLanes: List<CreateLaneRequest>
    ): Boolean {
        if (it.endingPoint == length)
            return true

        val currentIndex = it.indexFromLeft
        val currentRange = changeLanePosition..it.endingPoint
        val leftLane = lanes.find { it.indexFromLeft == currentIndex - 1 }
        val rightLane = lanes.find { it.indexFromLeft == currentIndex + 1 }

        if (changeLaneAndTryReachEndRecc(leftLane, visitedLanes, currentRange, lanes, length, it))
            return true

        if (changeLaneAndTryReachEndRecc(rightLane, visitedLanes, currentRange, lanes, length, it))
            return true

        return false
    }

    private fun changeLaneAndTryReachEndRecc(
        targetLane: CreateLaneRequest?,
        visitedLanes: List<CreateLaneRequest>,
        currentRange: IntRange,
        lanes: List<CreateLaneRequest>,
        length: Int,
        it: CreateLaneRequest
    ): Boolean {
        if (targetLane != null && !visitedLanes.contains(targetLane)) {
            val targetRange = targetLane.startingPoint..targetLane.endingPoint
            val rangeWhereNextToEachOther = currentRange.intersect(targetRange)
            if (rangeWhereNextToEachOther.isNotEmpty()) {
                val result =
                    tryReachEndRecc(targetLane, rangeWhereNextToEachOther.first(), lanes, length, visitedLanes + it)
                if (result)
                    return true
            }
        }
        return false
    }

    private fun validateRoadNode(request: CreateRoadNodeRequest) {
        val initialTurnDirectionsSpecified = request.turnDirections != null && request.turnDirections.isNotEmpty()
        val overrideTurnDirections = request.overrideTurnDirectionsTurnEverywhere

        if (request.type == RoadNodeType.INTERSECTION) {
            if (!initialTurnDirectionsSpecified && !overrideTurnDirections)
                errorService.add("Bad intersection configuration name='${request.name}' - specify either turn directions or turn on flag to override everywhere")
            else if (initialTurnDirectionsSpecified && overrideTurnDirections) {
                log.warn("Specified turn directions for node name='${request.name}' will be ignored!")
            }

            if (request.startingRoadsIds.isEmpty()) {
                errorService.add("Bad intersection configuration name='${request.name}' - intersection must have at least one road starting from it")
            }
        } else {
            if (initialTurnDirectionsSpecified || overrideTurnDirections) {
                log.warn("Even through node name='${request.name}' type is Gateway there were turnDirections or overrideTurnDirectionsTurnEverywhere specified - they will be ignored!")
            }
        }
    }

    private fun getTurnDirections(
        request: CreateRoadNodeRequest,
        lanes: Map<Long, LaneEntity>,
        roads: Map<Long, RoadEntity>
    ): List<TurnDirectionEntity> {
        if (request.type == RoadNodeType.GATEWAY) {
            return emptyList()
        }
        if (request.overrideTurnDirectionsTurnEverywhere) {
            return generateTurnDirectionEverywhere(roads, request)
        }
        return request.turnDirections!!.map { createTurnDirectionRequest ->
            TurnDirectionEntity(
                sourceLane = lanes[createTurnDirectionRequest.sourceLaneId]!!,
                destinationRoad = roads[createTurnDirectionRequest.destinationRoadId]!!
            )
        }
    }

    private fun generateTurnDirectionEverywhere(
        roads: Map<Long, RoadEntity>,
        createRoadNodeRequest: CreateRoadNodeRequest
    ): List<TurnDirectionEntity> {
        return createRoadNodeRequest.endingRoadsIds.asSequence()
            .map { roads[it]!! }
            .flatMap { road: RoadEntity -> road.lanes }
            .flatMap { lane ->
                createRoadNodeRequest.startingRoadsIds
                    .map { roads[it]!! }
                    .map { destinationRoad ->
                        TurnDirectionEntity(
                            sourceLane = lane,
                            destinationRoad = destinationRoad
                        )
                    }
            }
            .toList()
    }
}
