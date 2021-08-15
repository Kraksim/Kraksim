package pl.edu.agh.cs.kraksim.api.factory.nagel

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
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
import pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject.NagelRoadsAssert
import pl.edu.agh.cs.kraksim.gps.algorithms.RoadLengthGPS
import pl.edu.agh.cs.kraksim.repository.MapRepository
import pl.edu.agh.cs.kraksim.repository.entities.*

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

    @BeforeEach
    fun clearAndCreateMap(){
        try {
            mapRepository.deleteById(1)
        } catch (e: Error){

        }
        val lane = LaneEntity(
                startingPoint = 0,
                endingPoint = 400,
                indexFromLeft = 0
        )
        val road = RoadEntity(
                length = 400,
                lanes = listOf(lane)
        )
        val mapEntity = MapEntity(
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
    }

    @TestFactory
    fun `Given MapEntity, the factory parses it correctly to Lists of NagelRoads, NagelIntersections and NagelGateways`(){
        val mapEntity = mapRepository.getById(1)
        val ( roads, intersections, gateways ) = mapFactory.from(mapEntity)
        dynamicTest("Given list of roads, check if they are parsed correctly"){
            //given
            val roadEntity = mapEntity.roads.first()
            val laneEntity = roadEntity.lanes.first()
            val endNode = mapEntity.roadNodes[1]
            //when

            //then
            NagelRoadsAssert(roads)
                    .assertPhysicalLength(roadEntity)
                    .assertLane(laneEntity, roadEntity.id)
                    .assertEnd(endNode, roadEntity.id)
        }
        dynamicTest("Given list of intersections, check if they are parsed correctly"){

        }
    }
}
