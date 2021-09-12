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
import pl.edu.agh.cs.kraksim.controller.dto.PositionDTO
import pl.edu.agh.cs.kraksim.controller.mappers.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.controller.mappers.MapMapper
import pl.edu.agh.cs.kraksim.controller.mappers.SimulationMapper
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.repository.entities.*
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.*

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
                roads = listOf(roadEntity)
            ),
            statisticsEntities = listOf()
        )

        simulationStateEntity.simulation = simulationEntity

        val mapEntity = MapEntity(
            type = MapType.MAP,
            roadNodes = listOf(roadNodeEntity),
            roads = listOf()
        )

        val mapDTO = mapMapper.convertToDto(mapEntity, CycleAvoidingMappingContext())
        assertThat(mapDTO.roadNodes[0].position).isEqualTo(PositionDTO(0.0, 0.0))

        val simulationDTO = simulationMapper.convertToDTO(simulationEntity)

        assertThat(simulationDTO.expectedVelocity).isEqualTo(mapOf(0L to 1))
    }
}
