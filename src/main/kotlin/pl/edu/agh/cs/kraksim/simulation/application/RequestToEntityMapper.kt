package pl.edu.agh.cs.kraksim.simulation.application

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.simulation.domain.*
import pl.edu.agh.cs.kraksim.simulation.web.request.*
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase
import pl.edu.agh.cs.kraksim.trafficLight.web.request.CreateLightPhaseStrategyRequest
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.*
import pl.edu.agh.cs.kraksim.trafficState.domain.request.CreateGatewayStateRequest
import pl.edu.agh.cs.kraksim.trafficState.domain.request.CreateInitialSimulationStateRequest

@Component
class RequestToEntityMapper {

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
            endingRoads = createRoadNodeRequest.endingRoadsIds.map { id -> roads[id]!! },
            startingRoads = createRoadNodeRequest.startingRoadsIds.map { id -> roads[id]!! },
            turnDirections = createRoadNodeRequest.turnDirections.map { createTurnDirectionRequest ->
                TurnDirectionEntity(
                    sourceLane = lanes[createTurnDirectionRequest.sourceLaneId]!!,
                    destinationRoad = roads[createTurnDirectionRequest.destinationRoadId]!!
                )
            },
            name = createRoadNodeRequest.name
        )
}
