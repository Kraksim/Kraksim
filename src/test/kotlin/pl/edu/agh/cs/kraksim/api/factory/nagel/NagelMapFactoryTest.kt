package pl.edu.agh.cs.kraksim.api.factory.nagel

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject.NagelIntersectionsAssert
import pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject.NagelRoadNodeAssert
import pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject.NagelRoadsAssert
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.factory.NagelMapFactory
import pl.edu.agh.cs.kraksim.simulation.db.MapRepository
import pl.edu.agh.cs.kraksim.simulation.domain.*
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType

@Testcontainers
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NagelMapFactoryTest @Autowired constructor(
    val mapFactory: NagelMapFactory,
    val mapRepository: MapRepository
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

    fun createTestMap(): Long {
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

        val mapEntity = MapEntity(
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
            roads = roads,
            compatibleWith = listOf(
                MovementSimulationStrategyType.MULTI_LANE_NAGEL_SCHRECKENBERG,
                MovementSimulationStrategyType.NAGEL_SCHRECKENBERG
            )
        )
        return mapRepository.save(mapEntity).id
    }

    @TestFactory
    fun `Given MapEntity, the factory parses it correctly to Lists of NagelRoads, NagelIntersections and NagelGateways`(): List<DynamicTest> {
        val mapId = createTestMap()
        val mapEntity = mapRepository.findById(mapId).get()
        val (roads, intersections, gateways) = mapFactory.from(mapEntity)
        return listOf(
            dynamicTest("Given list of roads, check if they are parsed correctly") {
                // given
                val roadEntity = mapEntity.roads.first()
                val laneEntity = roadEntity.lanes.first()
                val endNode = mapEntity.roadNodes[1]
                // when

                // then
                NagelRoadsAssert(roads)
                    .assertPhysicalLength(roadEntity)
                    .assertLane(laneEntity, roadEntity.id)
                    .assertEnd(endNode, roadEntity.id)
            },
            dynamicTest("Given list of gateways, check if they are parsed correctly") {
                // given
                val gatewayEntity = mapEntity.roadNodes.first()
                // when

                // then
                NagelRoadNodeAssert(gateways)
                    .assertEndingAndStartingRoads(gatewayEntity)
            },
            dynamicTest("Given list of intersections, check if they are parsed correctly") {
                // given
                val intersectionEntity = mapEntity.roadNodes[1]
                NagelRoadNodeAssert(intersections)
                    .assertEndingAndStartingRoads(intersectionEntity)
                NagelIntersectionsAssert(intersections)
                    .assertTurningDirections(intersectionEntity.id, intersectionEntity.turnDirections)
            }
        )
    }
}
