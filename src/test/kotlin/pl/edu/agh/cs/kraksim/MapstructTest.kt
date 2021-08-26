package pl.edu.agh.cs.kraksim

import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers
import pl.edu.agh.cs.kraksim.controller.mappers.MapMapper
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.repository.entities.*
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.*

class MapstructTest {

    @Test
    fun `entity to dto conversion`() {
        val turnDirectionEntity = TurnDirectionEntity(0, 1)
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
            gps = GPSEntity(route = listOf(), type =GPSType.DIJKSTRA_ROAD_LENGTH)
        )
        val trafficLightEntity = TrafficLightEntity(0, listOf())
        val simulationStateEntity = SimulationStateEntity(
            turn = 0,
            carsOnMap = listOf(),
            trafficLights = listOf(trafficLightEntity),
            gatewaysStates = listOf(GatewayStateEntity(gatewayId = 0, collectedCars = listOf(carEntity), generators = listOf(
                GeneratorEntity(carsToRelease = 0, releaseDelay = 0, targetGatewayId = 0, gpsType =GPSType.DIJKSTRA_ROAD_LENGTH)
            ))),
            stateType = StateType.NAGEL_SCHRECKENBERG
        )



        val simulationEntity = SimulationEntity(
            simulationStateEntities = listOf(simulationStateEntity) as MutableList<SimulationStateEntity>,
            movementSimulationStrategy = MovementSimulationStrategyEntity(
                type =MovementSimulationStrategyType.NAGEL_SCHRECKENBERG,
                randomProvider =RandomProviderType.TRUE,
                slowDownProbability = 0.0,
                maxVelocity = 0
            ),
            simulationType =SimulationType.NAGEL_CORE, expectedVelocity = mapOf(0L to 1), lightPhaseStrategies = listOf(),
            mapEntity = MapEntity(
                type =MapType.MAP,
                roadNodes = listOf(roadNodeEntity),
                roads = listOf(roadEntity)
            ),
        )

        simulationStateEntity.simulation = simulationEntity

        val converter = Mappers.getMapper(MapMapper::class.java)

//        val simulationDTO = converter.convertToDTO(simulationEntity)

//        assertThat(simulationDTO.expectedVelocity).isEqualTo(mapOf(0L to 1))

        val mapEntity = MapEntity(
            type = MapType.MAP,
            roadNodes = listOf(roadNodeEntity),
            roads = listOf()
        )

        val mapDTO = converter.convertToDto(mapEntity)
    }
}


