package pl.edu.agh.cs.kraksim.simulation.application

import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import pl.edu.agh.cs.kraksim.common.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationDTO
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationEntity
import pl.edu.agh.cs.kraksim.trafficState.application.mapper.LightStateMapper
import pl.edu.agh.cs.kraksim.trafficState.application.mapper.MovementSimulationStrategyMapper
import pl.edu.agh.cs.kraksim.trafficState.application.mapper.SimulationStateMapper

@Mapper(uses = [SimulationStateMapper::class, MapMapper::class, MovementSimulationStrategyMapper::class, LightStateMapper::class])
interface SimulationMapper {

    /**
     * we add CycleAvoidingMappingContext, which is a hashmap that before it maps checks if it has saved instance mapped earlier,
     * it helps with circular dependency
     */
    @Mappings(
        value = [
            Mapping(source = "mapEntity", target = "mapDTO"),
            Mapping(source = "finished", target = "isFinished")
        ]
    )
    fun convertToDTO(simulationEntity: SimulationEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): SimulationDTO
}
