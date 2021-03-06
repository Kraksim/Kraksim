package pl.edu.agh.cs.kraksim.trafficState.application.mapper

import org.mapstruct.Context
import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.common.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.trafficState.domain.dto.LightPhaseStrategyDTO
import pl.edu.agh.cs.kraksim.trafficState.domain.dto.PhaseDTO
import pl.edu.agh.cs.kraksim.trafficState.domain.dto.TrafficLightDTO
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.LightPhaseStrategyEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.PhaseEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.TrafficLightEntity

@Mapper
interface LightStateMapper {
    fun convertTrafficLightToDto(trafficLightEntity: TrafficLightEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): TrafficLightDTO
    fun convertTrafficLightToEntity(trafficLightDTO: TrafficLightDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): TrafficLightEntity

    fun convertTrafficLightToDtos(trafficLightEntity: List<TrafficLightEntity>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<TrafficLightDTO>
    fun convertTrafficLightToEntities(trafficLightDTO: List<TrafficLightDTO>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<TrafficLightEntity>

    fun convertPhaseToDto(phaseEntity: PhaseEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): PhaseDTO
    fun convertPhaseToEntity(phaseDTO: PhaseDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): PhaseEntity

    fun convertPhaseToDtos(phaseEntity: List<PhaseEntity>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<PhaseDTO>
    fun convertPhaseToEntities(phaseDTO: List<PhaseDTO>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<PhaseEntity>

    fun convertLightPhaseStrategyToDto(lightPhaseStrategyEntity: LightPhaseStrategyEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): LightPhaseStrategyDTO
    fun convertLightPhaseStrategyToEntity(lightPhaseStrategyDTO: LightPhaseStrategyDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): LightPhaseStrategyEntity

    fun convertLightPhaseStrategyToDtos(lightPhaseStrategyEntity: List<LightPhaseStrategyEntity>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<LightPhaseStrategyDTO>
    fun convertLightPhaseStrategyToEntities(lightPhaseStrategyDTO: List<LightPhaseStrategyDTO>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<LightPhaseStrategyEntity>
}
