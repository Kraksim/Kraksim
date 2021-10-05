package pl.edu.agh.cs.kraksim.api.controller.mappers.trafficState

import org.mapstruct.Context
import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.api.controller.dto.trafficState.MovementSimulationStrategyDTO
import pl.edu.agh.cs.kraksim.api.controller.mappers.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.MovementSimulationStrategyEntity

@Mapper
interface MovementSimulationStrategyMapper {
    fun convertToDto(movementSimulationStrategyEntity: MovementSimulationStrategyEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): MovementSimulationStrategyDTO
    fun convertToEntity(movementSimulationStrategyDTO: MovementSimulationStrategyDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): MovementSimulationStrategyEntity
}
