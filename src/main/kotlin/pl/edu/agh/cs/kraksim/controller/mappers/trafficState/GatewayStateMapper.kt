package pl.edu.agh.cs.kraksim.controller.mappers.trafficState

import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.GatewayStateDTO
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.GeneratorDTO
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.GatewayStateEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.GeneratorEntity

@Mapper
interface GatewayStateMapper {
    fun convertToDto(gatewayStateEntity: GatewayStateEntity): GatewayStateDTO
    fun convertToEntity(gatewayStateDTO: GatewayStateDTO): GatewayStateEntity

    fun convertToDtos(gatewayStateEntity: List<GatewayStateEntity>): List<GatewayStateDTO>
    fun convertToEntities(gatewayStateDTO: List<GatewayStateDTO>): List<GatewayStateEntity>

    fun convertGeneratorToDto(generatorEntity: GeneratorEntity): GeneratorDTO
    fun convertGeneratorToEntity(generatorDTO: GeneratorDTO): GeneratorEntity

    fun convertGeneratorToDtos(generatorEntity: List<GeneratorEntity>): List<GeneratorDTO>
    fun convertGeneratorToEntitys(generatorDTO: List<GeneratorDTO>): List<GeneratorEntity>
}