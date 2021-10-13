package pl.edu.agh.cs.kraksim.trafficState.application.mapper

import org.mapstruct.Context
import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.common.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.trafficState.domain.dto.MovementSimulationStrategyDTO
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyEntity

@Mapper
interface MovementSimulationStrategyMapper {
    fun convertToDto(movementSimulationStrategyEntity: MovementSimulationStrategyEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): MovementSimulationStrategyDTO
    fun convertToEntity(movementSimulationStrategyDTO: MovementSimulationStrategyDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): MovementSimulationStrategyEntity
}
