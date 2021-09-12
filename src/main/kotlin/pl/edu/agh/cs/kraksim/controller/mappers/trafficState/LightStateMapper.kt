package pl.edu.agh.cs.kraksim.controller.mappers.trafficState

import org.mapstruct.Context
import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.LightPhaseStrategyDTO
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.PhaseDTO
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.TrafficLightDTO
import pl.edu.agh.cs.kraksim.controller.mappers.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.LightPhaseStrategyEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.PhaseEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.TrafficLightEntity

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

