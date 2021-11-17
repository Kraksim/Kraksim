package pl.edu.agh.cs.kraksim.simulation.domain

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.trafficState.domain.dto.LightPhaseStrategyDTO
import pl.edu.agh.cs.kraksim.trafficState.domain.dto.MovementSimulationStrategyDTO
import pl.edu.agh.cs.kraksim.trafficState.domain.dto.SimulationStateDTO

class SimulationDTO(
    var mapDTO: MapDTO,
    var simulationStateEntities: MutableList<SimulationStateDTO>,
    var movementSimulationStrategy: MovementSimulationStrategyDTO,
    var simulationType: SimulationType,
    var expectedVelocity: Map<RoadId, Velocity>,
    var lightPhaseStrategies: List<LightPhaseStrategyDTO>,
    var name: String = "",
    var isFinished: Boolean = false
) {
    var id: Long = 0
}
