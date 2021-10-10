package pl.edu.agh.cs.kraksim.controller.dto.basicInfo

import pl.edu.agh.cs.kraksim.repository.entities.SimulationType

class BasicSimulationInfoDTO(
    val id: Long,
    val name: String,
    val type: SimulationType,
    val mapId: Long
)
