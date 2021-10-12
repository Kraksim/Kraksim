package pl.edu.agh.cs.kraksim.trafficState.application.mapper

import org.mapstruct.Context
import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.common.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.simulation.application.SimulationMapper
import pl.edu.agh.cs.kraksim.trafficState.domain.dto.SimulationStateDTO
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.SimulationStateEntity

@Mapper(uses = [CarMapper::class, GatewayStateMapper::class, LightStateMapper::class, SimulationMapper::class])
interface SimulationStateMapper {
    fun convertToDto(simulationStateEntity: SimulationStateEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): SimulationStateDTO
    fun convertToEntity(simulationStateDTO: SimulationStateDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): SimulationStateEntity

    fun convertToDtos(simulationStateEntity: MutableList<SimulationStateEntity>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): MutableList<SimulationStateDTO>
    fun convertToEntities(simulationStateDTO: MutableList<SimulationStateDTO>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): MutableList<SimulationStateEntity>
}
