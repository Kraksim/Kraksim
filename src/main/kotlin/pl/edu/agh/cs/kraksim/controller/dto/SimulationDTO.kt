package pl.edu.agh.cs.kraksim.controller.dto

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.LightPhaseStrategyDTO
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.MovementSimulationStrategyDTO
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.SimulationStateDTO
import pl.edu.agh.cs.kraksim.repository.entities.SimulationType

class SimulationDTO(
    var mapDTO: MapDTO,
    var simulationStateEntities: MutableList<SimulationStateDTO>,
    var movementSimulationStrategy: MovementSimulationStrategyDTO,
    var simulationType: SimulationType,
    var expectedVelocity: Map<RoadId, Velocity>,
    var lightPhaseStrategies: List<LightPhaseStrategyDTO>
) {
    var id: Long = 0
}
