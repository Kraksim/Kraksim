package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.multilaneNagel.utils

import pl.edu.agh.cs.kraksim.common.intersection
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelRoad
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase

/*                                   ----\
    G(1) ------------> I(0) --------------> G(2)
         ----/
 */
fun getTwoRoadConnectedWithIntersectionMultiLaneSimulationState(
    trafficLightColor: TrafficLightPhase.LightColor = TrafficLightPhase.LightColor.GREEN
): NagelSimulationState {

    val state = MultiLaneNagelStateBuilder(0..0, 1..2)
        .connect(
            sourceId = 1, destinationId = 0,
            road = NagelRoad(id = 0, physicalLength = 20).apply {
                addLane(
                    laneId = 10,
                    indexFromLeft = 0,
                    physicalStartingPoint = 0,
                    physicalEndingPoint = 20
                )
                addLane(
                    laneId = 11,
                    indexFromLeft = 1,
                    physicalStartingPoint = 0,
                    physicalEndingPoint = 5
                )
            }
        )
        .connect(
            sourceId = 0, destinationId = 1,
            road = NagelRoad(id = 1, physicalLength = 20).apply {
                addLane(
                    laneId = 12,
                    indexFromLeft = 0,
                    physicalStartingPoint = 0,
                    physicalEndingPoint = 5
                )
                addLane(
                    laneId = 13,
                    indexFromLeft = 1,
                    physicalStartingPoint = 0,
                    physicalEndingPoint = 20
                )
            }
        )
        .turnDirection(intersectionId = 0, sourceRoadId = 0, sourceLaneId = 10, destinationRoadId = 1)
        .build()

    state.intersection(0).phases.forEach { (_, phase) ->
        phase.state = trafficLightColor
    }
    return state
}
