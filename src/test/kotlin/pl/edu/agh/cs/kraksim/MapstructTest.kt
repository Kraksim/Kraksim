package pl.edu.agh.cs.kraksim

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
import pl.edu.agh.cs.kraksim.common.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.simulation.application.MapMapper
import pl.edu.agh.cs.kraksim.simulation.application.SimulationMapper
import pl.edu.agh.cs.kraksim.simulation.domain.*
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.*

@Testcontainers
@SpringBootTest
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MapstructTest @Autowired constructor(
    val simulationMapper: SimulationMapper,
    val mapMapper: MapMapper
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

    @Test
    fun `entity to dto conversion`() {
        val laneEntity = LaneEntity(0, 20, 0)
        val roadEntity = RoadEntity(20, listOf(laneEntity))
        val roadNodeEntity = RoadNodeEntity(
            RoadNodeType.GATEWAY,
            PositionEntity(0.0, 0.0),
            listOf(),
            listOf(),
            listOf()
        )
        val carEntity = CarEntity(
            carId = 0,
            velocity = 0,
            currentLaneId = null,
            positionRelativeToStart = 0,
            gps = GPSEntity(route = listOf(), type = GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        val trafficLightEntity = TrafficLightEntity(0, listOf())
        val simulationStateEntity = SimulationStateEntity(
            turn = 0,
            carsOnMap = listOf(),
            trafficLights = listOf(trafficLightEntity),
            gatewaysStates = listOf(
                GatewayStateEntity(
                    gatewayId = 0, collectedCars = listOf(carEntity),
                    generators = listOf(
                        GeneratorEntity(
                            carsToRelease = 0,
                            releaseDelay = 0,
                            targetGatewayId = 0,
                            gpsType = GPSType.DIJKSTRA_ROAD_LENGTH,
                            lastCarReleasedTurnsAgo = 0
                        )
                    )
                )
            ),
            stateType = StateType.NAGEL_SCHRECKENBERG
        )

        val simulationEntity = SimulationEntity(
            name = "TEST",
            simulationStateEntities = listOf(simulationStateEntity) as MutableList<SimulationStateEntity>,
            movementSimulationStrategy = MovementSimulationStrategyEntity(
                type = MovementSimulationStrategyType.NAGEL_SCHRECKENBERG,
                randomProvider = RandomProviderType.TRUE,
                slowDownProbability = 0.0,
                maxVelocity = 0
            ),
            simulationType = SimulationType.NAGEL_CORE,
            expectedVelocity = mapOf(0L to 1),
            lightPhaseStrategies = listOf(),
            mapEntity = MapEntity(
                type = MapType.MAP,
                roadNodes = listOf(roadNodeEntity),
                roads = listOf(roadEntity),
                compatibleWith = listOf(MovementSimulationStrategyType.MULTI_LANE_NAGEL_SCHRECKENBERG, MovementSimulationStrategyType.NAGEL_SCHRECKENBERG)
            ),
            statisticsEntities = listOf()
        )

        val mapEntity = MapEntity(
            type = MapType.MAP,
            roadNodes = listOf(roadNodeEntity),
            roads = listOf(),
            compatibleWith = listOf(MovementSimulationStrategyType.MULTI_LANE_NAGEL_SCHRECKENBERG, MovementSimulationStrategyType.NAGEL_SCHRECKENBERG)
        )

        val mapDTO = mapMapper.convertToDto(mapEntity, CycleAvoidingMappingContext())
        assertThat(mapDTO.roadNodes[0].position).isEqualTo(PositionDTO(0.0, 0.0))

        val simulationDTO = simulationMapper.convertToDTO(simulationEntity)

        assertThat(simulationDTO.expectedVelocity).isEqualTo(mapOf(0L to 1))
    }
}
