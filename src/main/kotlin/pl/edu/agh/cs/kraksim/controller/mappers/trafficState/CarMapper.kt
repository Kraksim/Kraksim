package pl.edu.agh.cs.kraksim.controller.mappers.trafficState

import org.mapstruct.Context
import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.CarDTO
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.GPSDTO
import pl.edu.agh.cs.kraksim.controller.mappers.CycleAvoidingMappingContext
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.CarEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.GPSEntity

@Mapper
interface CarMapper{
    fun convertToDto(carEntity: CarEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): CarDTO
    fun convertToEntity(carDTO: CarDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): CarEntity

    fun convertToDtos(carEntity: List<CarEntity>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<CarDTO>
    fun convertToEntities(carDTO: List<CarDTO>, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): List<CarEntity>

    fun convertGpsToDto(gpsEntity: GPSEntity, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): GPSDTO
    fun convertGpsToEntity(gpsdto: GPSDTO, @Context context: CycleAvoidingMappingContext = CycleAvoidingMappingContext()): GPSEntity
}