package pl.edu.agh.cs.kraksim.api

import org.assertj.core.api.Assertions.assertThat
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
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.gps.algorithms.RoadLengthGPS
import pl.edu.agh.cs.kraksim.repository.CarRepository
import pl.edu.agh.cs.kraksim.repository.MapRepository
import pl.edu.agh.cs.kraksim.repository.SimulationRepository
import pl.edu.agh.cs.kraksim.repository.entities.*
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.*

@Testcontainers
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class ServiceTest @Autowired constructor(
    val simprep: SimulationRepository,
    val roadLengthGPS: RoadLengthGPS,
    val service: Service,
    val mapRepository: MapRepository,
    val carRepository: CarRepository
) {

    //todo zjebane to jest cos niby przechodzi ale sie nie zatrzymuje ten test, ale basic idea containerów testowych
    // jest wjebana, pewnie można tro ulepszyc czy coś, moze na stacku zapostuje potem
    // skurwione te testy troche ugh

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

    @Test
    fun integrationTestDemo() {
        var lane = LaneEntity(
            startingPoint = 0,
            endingPoint = 400,
            indexFromLeft = 0
        )
        val road = RoadEntity(
            length = 400,
            lanes = listOf(lane)
        )

        var mapEntity = MapEntity(
            type = MapType.MAP,
            roadNodes = listOf(
                RoadNodeEntity(
                    type = RoadNodeType.GATEWAY,
                    position = PositionEntity(1.0, 1.0),
                    endingRoads = emptyList(),
                    startingRoads = listOf(road),
                    turnDirections = emptyList()
                ),
                RoadNodeEntity(
                    type = RoadNodeType.GATEWAY,
                    position = PositionEntity(21.0, 1.0),
                    endingRoads = listOf(road),
                    startingRoads = emptyList(),
                    turnDirections = emptyList()
                )
            ),
            roads = listOf(road)
        )
        mapRepository.save(mapEntity)

        mapEntity = mapRepository.getById(1)
        lane = road.lanes.first()

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
            lightPhaseStrategies = emptyList()
        )

        val simulationStateEntity = SimulationStateEntity(
            turn = 0,
            trafficLights = emptyList(),
            simulation = simulationEntity,
            stateType = StateType.NAGEL_SCHRECKENBERG,
            gatewaysStates = emptyList(),
            carsOnMap = listOf(
                CarEntity(
                    carId = 1,
                    velocity = 2,
                    currentLaneId = lane.id,
                    positionRelativeToStart = 30,
                    gps = GPSEntity(
                        type = GPSType.DIJKSTRA_ROAD_LENGTH,
                        route = emptyList()
                    )
                ),
                CarEntity(
                    carId = 2,
                    velocity = 6,
                    currentLaneId = lane.id,
                    positionRelativeToStart = 50,
                    gps = GPSEntity(
                        type = GPSType.DIJKSTRA_ROAD_LENGTH,
                        route = emptyList()
                    )
                )
            )
        )
        simulationEntity.simulationStateEntities.add(simulationStateEntity)
        simulationEntity = simprep.save(simulationEntity)

        service.simulateStep(simulationEntity.id, 1)

        val count = carRepository.count()
        assertThat(count).isEqualTo(4)

    }

}