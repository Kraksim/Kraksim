package pl.edu.agh.cs.kraksim.api.controller.mappers.trafficState

import org.mapstruct.Context
import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.api.controller.dto.trafficState.GatewayStateDTO
import pl.edu.agh.cs.kraksim.api.controller.dto.trafficState.GeneratorDTO
import pl.edu.agh.cs.kraksim.api.controller.mappers.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.GatewayStateEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.GeneratorEntity

@Mapper
interface GatewayStateMapper {
    fun convertToDto(gatewayStateEntity: GatewayStateEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): GatewayStateDTO
    fun convertToEntity(gatewayStateDTO: GatewayStateDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): GatewayStateEntity

    fun convertToDtos(gatewayStateEntity: List<GatewayStateEntity>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<GatewayStateDTO>
    fun convertToEntities(gatewayStateDTO: List<GatewayStateDTO>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<GatewayStateEntity>

    fun convertGeneratorToDto(generatorEntity: GeneratorEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): GeneratorDTO
    fun convertGeneratorToEntity(generatorDTO: GeneratorDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): GeneratorEntity

    fun convertGeneratorToDtos(generatorEntity: List<GeneratorEntity>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<GeneratorDTO>
    fun convertGeneratorToEntities(generatorDTO: List<GeneratorDTO>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<GeneratorEntity>
}
