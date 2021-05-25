package pl.edu.agh.cs.kraksim.nagelCore

import kotlin.math.roundToInt

class NagelLane(
    private val id: Long,
    startingPoint: Int,
    endingPoint: Int,
    val indexFromLeft: Int
) {
    val cars: MutableList<Car> = ArrayList();
    var cellsCount: Int

    init {
        val length = endingPoint - startingPoint;
        cellsCount = (length / AVERAGE_CAR_LENGTH).roundToInt()
    }

    fun addCar(car: Car) {
        this.cars.add(car)
    }

    companion object {
        const val AVERAGE_CAR_LENGTH = 4.5
    }
}