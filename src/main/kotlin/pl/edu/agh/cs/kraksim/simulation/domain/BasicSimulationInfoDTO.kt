package pl.edu.agh.cs.kraksim.simulation.domain

class BasicSimulationInfoDTO(
    val id: Long,
    val name: String,
    val type: SimulationType,
    val mapId: Long,
    var isFinished: Boolean,
    var turn: Long
)
