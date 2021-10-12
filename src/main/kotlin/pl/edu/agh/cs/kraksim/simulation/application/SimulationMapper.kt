package pl.edu.agh.cs.kraksim.simulation.application

import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import pl.edu.agh.cs.kraksim.common.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.controller.dto.SimulationDTO
import pl.edu.agh.cs.kraksim.controller.mappers.trafficState.LightStateMapper
import pl.edu.agh.cs.kraksim.controller.mappers.trafficState.MovementSimulationStrategyMapper
import pl.edu.agh.cs.kraksim.controller.mappers.trafficState.SimulationStateMapper
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationEntity

@Mapper(uses = [SimulationStateMapper::class, MapMapper::class, MovementSimulationStrategyMapper::class, LightStateMapper::class])
interface SimulationMapper {

    /**
     * we add CycleAvoidingMappingContext, which is a hashmap that before it maps checks if it has saved instance mapped earlier,
     * it helps with circular dependency
     */
    @Mapping(source = "mapEntity", target = "mapDTO")
    fun convertToDTO(simulationEntity: SimulationEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): SimulationDTO
}
