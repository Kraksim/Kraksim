package pl.edu.agh.cs.kraksim.nagelCore

import kotlin.math.roundToInt

class NagelLane(
    private val id: Long,
    startingPoint: Int,
    endingPoint: Int,
    val indexFromLeft: Int,
    val parentRoad: NagelRoad
) {
    val cars: MutableList<NagelCar> =
        ArrayList(); // lista ma same auta, nie ma pustyc komórek. posortowana bo chcemy meic odtep łątwy do auta przed danym autem
    var cellsCount: Int

    init {
        val length = endingPoint - startingPoint;
        cellsCount = (length / AVERAGE_CAR_LENGTH).roundToInt()
    }

    fun addCar(car: NagelCar) {
        this.cars.add(car)
    }

    companion object {
        const val AVERAGE_CAR_LENGTH = 4.5
    }
}