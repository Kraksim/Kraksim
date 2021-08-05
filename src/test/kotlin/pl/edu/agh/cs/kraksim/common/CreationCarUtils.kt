package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.core.state.Car
import pl.edu.agh.cs.kraksim.gps.GPS
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelCar

fun createListOfCars(amount: Int, velocity: Int, spaces: Int): List<Car> {
    return (0 until amount)
        .asSequence()
        .map { num ->
            val car = NagelCar(id = num.toLong(), velocity, gps = mockGps())
            car.positionRelativeToStart = num + (spaces * num)
            return@map car
        }.toList().reversed()
}

fun mockGps() = GPS(route = ArrayList(), type = GPSType.DIJKSTRA_ROAD_LENGTH)
