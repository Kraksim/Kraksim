package pl.edu.agh.cs.kraksim.api.factory.nagel

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import pl.edu.agh.cs.kraksim.api.StatisticsService
import pl.edu.agh.cs.kraksim.api.factory.LightPhaseManagerFactory
import pl.edu.agh.cs.kraksim.api.factory.MovementSimulationStrategyFactory
import pl.edu.agh.cs.kraksim.api.factory.SimulationFactory
import pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject.NagelSimulationAssert
import pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject.NagelSimulationStateAssert
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.nagelCore.NagelSimulation
import pl.edu.agh.cs.kraksim.repository.MapRepository
import pl.edu.agh.cs.kraksim.repository.SimulationRepository
import pl.edu.agh.cs.kraksim.repository.entities.*
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.*
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase

@Testcontainers
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NagelSimulationStateFactoryTest @Autowired constructor(
    val simulationRepository: SimulationRepository,
    val mapRepository: MapRepository,
    val simulationFactory: SimulationFactory,
    val stateFactory: NagelSimulationStateFactory,
    val movementSimulationStrategyFactory: MovementSimulationStrategyFactory,
    val lightPhaseManagerFactory: LightPhaseManagerFactory,
    val statisticsService: StatisticsService
) {

    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:latest")

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }

    fun createTestSimulation(): Long {
        val lanes = (0 until 3).toList().map {
            LaneEntity(
                startingPoint = 0,
                endingPoint = 400,
                indexFromLeft = it
            )
        }

        val roads = (0 until 3).toList().map {
            RoadEntity(
                length = 400,
                lanes = listOf(lanes[it])
            )
        }

        var mapEntity = MapEntity(
            type = MapType.MAP,
            roadNodes = listOf(
                RoadNodeEntity(
                    type = RoadNodeType.GATEWAY,
                    position = PositionEntity(21.0, 21.0),
                    endingRoads = emptyList(),
                    startingRoads = listOf(roads[0]),
                    turnDirections = emptyList()
                ),
                RoadNodeEntity(
                    type = RoadNodeType.INTERSECTION,
                    position = PositionEntity(41.0, 21.0),
                    endingRoads = listOf(roads[0]),
                    startingRoads = listOf(roads[1], roads[2]),
                    turnDirections = listOf(
                        TurnDirectionEntity(
                            sourceLane = lanes[0],
                            destinationRoad = roads[1]
                        ),
                        TurnDirectionEntity(
                            sourceLane = lanes[0],
                            destinationRoad = roads[2]
                        )
                    )
                ),
                RoadNodeEntity(
                    type = RoadNodeType.GATEWAY,
                    position = PositionEntity(41.0, 41.0),
                    endingRoads = listOf(roads[1]),
                    startingRoads = emptyList(),
                    turnDirections = emptyList()
                ),
                RoadNodeEntity(
                    type = RoadNodeType.GATEWAY,
                    position = PositionEntity(21.0, 1.0),
                    endingRoads = listOf(roads[2]),
                    startingRoads = emptyList(),
                    turnDirections = emptyList()
                ),
            ),
            roads = roads
        )
        mapEntity = mapRepository.save(mapEntity)
        val firstLane = mapEntity.roads.first()

        val simulationEntity = SimulationEntity(
            mapEntity = mapEntity,
            simulationStateEntities = ArrayList(),
            movementSimulationStrategy = MovementSimulationStrategyEntity(
                type = MovementSimulationStrategyType.NAGEL_SCHRECKENBERG,
                randomProvider = RandomProviderType.TRUE,
                slowDownProbability = 0.3,
                maxVelocity = 6
            ),
            simulationType = SimulationType.NAGEL_CORE,
            expectedVelocity = emptyMap(),
            lightPhaseStrategies = listOf(
                LightPhaseStrategyEntity(
                    algorithm = AlgorithmType.TURN_BASED,
                    turnLength = 5,
                    intersections = listOf(mapEntity.roadNodes[1].id)
                )
            ),
            statisticsEntities = ArrayList()
        )

        val simulationStateEntity = SimulationStateEntity(
            turn = 0,
            trafficLights = listOf(
                TrafficLightEntity(
                    intersectionId = mapEntity.roadNodes[1].id,
                    phases = listOf(
                        PhaseEntity(
                            laneId = mapEntity.roads[0].lanes.first().id,
                            state = TrafficLightPhase.LightColor.GREEN,
                            phaseTime = 5
                        )
                    )
                )
            ),
            stateType = StateType.NAGEL_SCHRECKENBERG,
            gatewaysStates = listOf(
                GatewayStateEntity(
                    gatewayId = mapEntity.roadNodes.first().id,
                    collectedCars = emptyList(),
                    generators = listOf(
                        GeneratorEntity(
                            lastCarReleasedTurnsAgo = 0,
                            releaseDelay = 4,
                            carsToRelease = 15,
                            targetGatewayId = mapEntity.roadNodes.last().id,
                            gpsType = GPSType.DIJKSTRA_ROAD_LENGTH
                        )
                    )
                )
            ),
            carsOnMap = listOf(
                CarEntity(
                    carId = 1,
                    velocity = 2,
                    currentLaneId = firstLane.id,
                    positionRelativeToStart = 30,
                    gps = GPSEntity(
                        type = GPSType.DIJKSTRA_ROAD_LENGTH,
                        route = emptyList()
                    )
                ),
                CarEntity(
                    carId = 2,
                    velocity = 6,
                    currentLaneId = firstLane.id,
                    positionRelativeToStart = 50,
                    gps = GPSEntity(
                        type = GPSType.DIJKSTRA_ROAD_LENGTH,
                        route = emptyList()
                    )
                )
            ),
            simulation = simulationEntity
        )
        simulationEntity.simulationStateEntities.add(simulationStateEntity)
        return simulationRepository.save(simulationEntity).id
    }

    @Test
    fun `Given SimulationEntity, the factory parses it correctly to Simulation`() {
        // given
        val simulationId = createTestSimulation()
        val simulationEntity = simulationRepository.findById(simulationId).get()
        // when
        val simulationState = stateFactory.from(simulationEntity)
        val movementStrategy =
            movementSimulationStrategyFactory.from(simulationEntity.movementSimulationStrategy)
        val lightPhaseManager =
            lightPhaseManagerFactory.from(simulationState, simulationEntity.lightPhaseStrategies)
        val statisticsManager = statisticsService.createStatisticsManager(
            simulationEntity.statisticsEntities,
            simulationEntity.expectedVelocity
        )

        val simulation = simulationFactory.from(
            simulationType = simulationEntity.simulationType,
            simulationState = simulationState,
            movementStrategy = movementStrategy,
            lightPhaseManager = lightPhaseManager,
            statisticsManager = statisticsManager
        )

        // then
        NagelSimulationAssert(simulation = simulation as NagelSimulation)
            .assertExpectedVelocities(simulationEntity)
            .assertGateways(simulationEntity.latestTrafficStateEntity)
            .assertLightPhaseStrategies(simulationEntity)
            .assertMovementSimulationStrategy(simulationEntity.movementSimulationStrategy)

        NagelSimulationStateAssert(simulation.state)
            .assertCarsState(simulationEntity.latestTrafficStateEntity)
            .assertTurn(simulationEntity.latestTrafficStateEntity)
            .assertTrafficLightPhases(simulationEntity.latestTrafficStateEntity)
    }
}
