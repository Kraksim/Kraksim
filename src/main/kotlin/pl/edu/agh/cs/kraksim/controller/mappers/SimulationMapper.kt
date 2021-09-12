package pl.edu.agh.cs.kraksim.controller.mappers

import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.controller.dto.SimulationDTO
import pl.edu.agh.cs.kraksim.controller.mappers.trafficState.LightStateMapper
import pl.edu.agh.cs.kraksim.controller.mappers.trafficState.MovementSimulationStrategyMapper
import pl.edu.agh.cs.kraksim.controller.mappers.trafficState.SimulationStateMapper
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity

@Mapper(uses = [SimulationStateMapper::class, MapMapper::class, MovementSimulationStrategyMapper::class, LightStateMapper::class])
interface SimulationMapper {

    fun convertToDTO(simulationEntity: SimulationEntity): SimulationDTO
    fun convertToEntity(simulationDTO: SimulationDTO): SimulationEntity
}