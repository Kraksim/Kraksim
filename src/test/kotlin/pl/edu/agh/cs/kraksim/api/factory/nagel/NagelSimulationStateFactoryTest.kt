package pl.edu.agh.cs.kraksim.api.factory.nagel

import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import pl.edu.agh.cs.kraksim.api.SimulationService
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.repository.CarRepository
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
class NagelSimulationStateFactoryTest(
        val simulationRepository: SimulationRepository,
        val mapRepository: MapRepository,
        val carRepository: CarRepository,
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

    @BeforeEach
    fun createTestSimulation(){
        try {
            simulationRepository.deleteById(1)
        } catch (e: Exception){

        }
        val lanes = (0 until 3).toList().map { LaneEntity(
                startingPoint = 0,
                endingPoint = 400,
                indexFromLeft = it
        ) }

        val roads = (0 until 3).toList().map { RoadEntity(
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
        mapRepository.save(mapEntity)
        mapEntity = mapRepository.getById(1)
        var firstLane = mapEntity.roads.first()

        var simulationEntity = SimulationEntity(
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
                )
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
                simulation = simulationEntity,
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
                )
        )
        simulationEntity.simulationStateEntities.add(simulationStateEntity)
        simulationRepository.save(simulationEntity)
    }
}
