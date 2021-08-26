package pl.edu.agh.cs.kraksim.controller.mappers.trafficState

import org.mapstruct.Mapper
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.CarDTO
import pl.edu.agh.cs.kraksim.controller.dto.trafficState.GPSDTO
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.CarEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.GPSEntity

@Mapper
interface CarMapper{
    fun convertToDto(carEntity: CarEntity): CarDTO
    fun convertToEntity(carDTO: CarDTO): CarEntity

    fun convertToDtos(carEntity: List<CarEntity>): List<CarDTO>
    fun convertToEntitys(carDTO: List<CarDTO>): List<CarEntity>

    fun convertGpsToDto(gpsEntity: GPSEntity): GPSDTO
    fun convertGpsToEntity(gpsdto: GPSDTO): GPSEntity
}