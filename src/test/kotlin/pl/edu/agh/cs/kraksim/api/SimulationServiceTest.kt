package pl.edu.agh.cs.kraksim.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.simulation.application.SimulationService
import pl.edu.agh.cs.kraksim.simulation.db.MapRepository
import pl.edu.agh.cs.kraksim.simulation.db.SimulationRepository
import pl.edu.agh.cs.kraksim.simulation.domain.*
import pl.edu.agh.cs.kraksim.trafficState.db.CarRepository
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.*

@Testcontainers
@SpringBootTest
@Transactional
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class SimulationServiceTest @Autowired constructor(
    val simulationRepository: SimulationRepository,
    val simulationService: SimulationService,
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

    fun createTestSimulation(): Long {
        var lane = LaneEntity(
            startingPoint = 0,
            endingPoint = 400,
            indexFromLeft = 0
        )
        val road = RoadEntity(
            length = 400,
            lanes = arrayListOf(lane)
        )

        var mapEntity = MapEntity(
            type = MapType.MAP,
            roadNodes = arrayListOf(
                RoadNodeEntity(
                    type = RoadNodeType.GATEWAY,
                    position = PositionEntity(1.0, 1.0),
                    endingRoads = ArrayList(),
                    startingRoads = arrayListOf(road),
                    turnDirections = ArrayList()
                ),
                RoadNodeEntity(
                    type = RoadNodeType.GATEWAY,
                    position = PositionEntity(21.0, 1.0),
                    endingRoads = arrayListOf(road),
                    startingRoads = ArrayList(),
                    turnDirections = ArrayList()
                )
            ),
            roads = arrayListOf(road),
            compatibleWith = listOf(MovementSimulationStrategyType.MULTI_LANE_NAGEL_SCHRECKENBERG, MovementSimulationStrategyType.NAGEL_SCHRECKENBERG)
        )

        mapEntity = mapRepository.save(mapEntity)
        lane = road.lanes.first()

        var simulationEntity = SimulationEntity(
            name = "TEST",
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
            lightPhaseStrategies = ArrayList(),
            statisticsEntities = ArrayList()
        )

        val simulationStateEntity = SimulationStateEntity(
            turn = 0,
            trafficLights = ArrayList(),
            stateType = StateType.NAGEL_SCHRECKENBERG,
            gatewaysStates = ArrayList(),
            carsOnMap = arrayListOf(
                CarEntity(
                    carId = 1,
                    velocity = 2,
                    currentLaneId = lane.id,
                    positionRelativeToStart = 30,
                    gps = GPSEntity(
                        type = GPSType.DIJKSTRA_ROAD_LENGTH,
                        route = ArrayList()
                    ), brakeLightOn = null
                ),
                CarEntity(
                    carId = 2,
                    velocity = 6,
                    currentLaneId = lane.id,
                    positionRelativeToStart = 50,
                    gps = GPSEntity(
                        type = GPSType.DIJKSTRA_ROAD_LENGTH,
                        route = ArrayList()
                    ), brakeLightOn = null
                )
            )
        )
        simulationEntity.simulationStateEntities.add(simulationStateEntity)
        simulationEntity = simulationRepository.save(simulationEntity)
        return simulationEntity.id
    }

    @Test
    fun `Given amount of turns in a simulation, check if amount of CarEntities representing one car is equal to it`() {
        // given
        val simulationId = createTestSimulation()
        // when
        simulationService.simulateStep(simulationId)
        // then
        val count = carRepository.findCarEntitiesByCarId(1).count()
        assertThat(count).isEqualTo(2)
    }

    @Test
    fun `Given amount of turns in a simulation, check if amount of SimulationEntities match amount of turn`() {
        // given
        val simulationId = createTestSimulation()
        // when
        simulationService.simulateStep(simulationId)
        // then
        val count = simulationRepository.findById(simulationId).get().simulationStateEntities.count()
        assertThat(count).isEqualTo(2)
    }

    @Test
    fun `Given simulation entity, check if turns are assigned correctly`() {
        // given
        val simulationId = createTestSimulation()
        // when
        simulationService.simulateStep(simulationId)
        // then
        simulationRepository.findById(simulationId).get().simulationStateEntities
            .forEachIndexed { index, simulationState -> assertThat(simulationState.turn).isEqualTo(index.toLong()) }
    }
}
