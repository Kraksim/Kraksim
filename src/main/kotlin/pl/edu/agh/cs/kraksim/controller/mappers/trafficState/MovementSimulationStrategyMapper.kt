package pl.edu.agh.cs.kraksim.controller.mappers.trafficState

import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.MovementSimulationStrategyDTO
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.MovementSimulationStrategyEntity

@Mapper
interface MovementSimulationStrategyMapper {
    fun convertToDto(movementSimulationStrategyEntity: MovementSimulationStrategyEntity): MovementSimulationStrategyDTO
    fun convertToEntity(movementSimulationStrategyDTO: MovementSimulationStrategyDTO): MovementSimulationStrategyEntity
}