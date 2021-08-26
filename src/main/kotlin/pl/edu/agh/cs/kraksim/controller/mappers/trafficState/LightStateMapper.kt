package pl.edu.agh.cs.kraksim.controller.mappers.trafficState

import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.LightPhaseStrategyDTO
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.PhaseDTO
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.TrafficLightDTO
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.LightPhaseStrategyEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.PhaseEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.TrafficLightEntity

@Mapper
interface LightStateMapper {
    fun convertTrafficLightToDto(trafficLightEntity: TrafficLightEntity): TrafficLightDTO
    fun convertTrafficLightToEntity(trafficLightDTO: TrafficLightDTO): TrafficLightEntity

    fun convertTrafficLightToDtos(trafficLightEntity: List<TrafficLightEntity>): List<TrafficLightDTO>
    fun convertTrafficLightToEntities(trafficLightDTO: List<TrafficLightDTO>): List<TrafficLightEntity>

    fun convertPhaseToDto(phaseEntity: PhaseEntity): PhaseDTO
    fun convertPhaseToEntity(phaseDTO: PhaseDTO): PhaseEntity

    fun convertPhaseToDtos(phaseEntity: List<PhaseEntity>): List<PhaseDTO>
    fun convertPhaseToEntities(phaseDTO: List<PhaseDTO>): List<PhaseEntity>

    fun convertLightPhaseStrategyToDto(lightPhaseStrategyEntity: LightPhaseStrategyEntity): LightPhaseStrategyDTO
    fun convertLightPhaseStrategyToEntity(lightPhaseStrategyDTO: LightPhaseStrategyDTO): LightPhaseStrategyEntity

    fun convertLightPhaseStrategyToDtos(lightPhaseStrategyEntity: List<LightPhaseStrategyEntity>): List<LightPhaseStrategyDTO>
    fun convertLightPhaseStrategyToEntities(lightPhaseStrategyDTO: List<LightPhaseStrategyDTO>): List<LightPhaseStrategyEntity>
}

