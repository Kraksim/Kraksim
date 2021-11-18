package pl.edu.agh.cs.kraksim.simulation.domain

import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType

class BasicSimulationInfoDTO(
    val id: Long,
    val name: String,
    val type: SimulationType,
    val mapId: Long,
    var isFinished: Boolean,
    var turn: Long,
    var movementSimulationStrategyType: MovementSimulationStrategyType
)
