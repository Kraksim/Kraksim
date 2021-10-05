package pl.edu.agh.cs.kraksim.api.controller.mappers

import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import pl.edu.agh.cs.kraksim.api.controller.dto.SimulationDTO
import pl.edu.agh.cs.kraksim.api.controller.mappers.trafficState.LightStateMapper
import pl.edu.agh.cs.kraksim.api.controller.mappers.trafficState.MovementSimulationStrategyMapper
import pl.edu.agh.cs.kraksim.api.controller.mappers.trafficState.SimulationStateMapper
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity

@Mapper(uses = [SimulationStateMapper::class, MapMapper::class, MovementSimulationStrategyMapper::class, LightStateMapper::class])
interface SimulationMapper {

    /**
     * we add CycleAvoidingMappingContext, which is a hashmap that before it maps checks if it has saved instance mapped earlier,
     * it helps with circular dependency
     */
    @Mapping(source = "mapEntity", target = "mapDTO")
    fun convertToDTO(simulationEntity: SimulationEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): SimulationDTO

    @Mapping(source = "mapDTO", target = "mapEntity")
    fun convertToEntity(simulationDTO: SimulationDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): SimulationEntity
}
