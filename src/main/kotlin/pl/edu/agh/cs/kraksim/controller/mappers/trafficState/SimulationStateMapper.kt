package pl.edu.agh.cs.kraksim.controller.mappers.trafficState

import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.SimulationStateDTO
import pl.edu.agh.cs.kraksim.controller.mappers.SimulationMapper
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.SimulationStateEntity

@Mapper(uses = [CarMapper::class, GatewayStateMapper::class, LightStateMapper::class, SimulationMapper::class])
interface SimulationStateMapper {
    fun convertToDto(simulationStateEntity: SimulationStateEntity): SimulationStateDTO
    fun convertToEntity(simulationStateDTO: SimulationStateDTO): SimulationStateEntity

    fun convertToDtos(simulationStateEntity: MutableList<SimulationStateEntity>): MutableList<SimulationStateDTO>
    fun convertToEntitys(simulationStateDTO: MutableList<SimulationStateDTO>): MutableList<SimulationStateEntity>
}