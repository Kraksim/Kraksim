package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.common.gps.MockRoadGps
import pl.edu.agh.cs.kraksim.core.state.Car
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelCar
import java.util.stream.IntStream
import kotlin.streams.asSequence

fun createListOfCars(amount: Int, velocity: Int, spaces: Int): List<Car> {
    return (0 until amount)
        .asSequence()
        .map { num ->
            var car = NagelCar(velocity, gps = MockRoadGps())
            car.positionRelativeToStart = num + (spaces * num)
            return@map car
        }.toList()
}
