package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.core.state.Car
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelCar
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.GPSType

fun createListOfCars(amount: Int, velocity: Int, spaces: Int): List<Car> {
    return (0 until amount)
        .asSequence()
        .map { num ->
            val car = NagelCar(id = num.toLong(), velocity, gps = GPS(type = GPSType.DIJKSTRA_ROAD_LENGTH))
            car.positionRelativeToStart = num + (spaces * num)
            return@map car
        }.toList().reversed()
}
