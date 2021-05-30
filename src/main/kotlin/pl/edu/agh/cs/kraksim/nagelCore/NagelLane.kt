package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.comon.addToFront
import kotlin.math.roundToInt

class NagelLane(
    private val id: Long,
    val indexFromLeft: Int,
    val parentRoad: NagelRoad,
    startingPoint: Int,
    endingPoint: Int,
) {
    // lista ma same auta, nie ma pustyc komórek. posortowana bo chcemy meic odtep łątwy do auta przed danym autem
    val cars: MutableList<NagelCar> = ArrayList()

    val length: Int = endingPoint - startingPoint
    val cellsCount: Int = (length / AVERAGE_CAR_LENGTH).roundToInt()

    fun addCar(car: NagelCar) {
        this.cars.addToFront(car)
//        this.cars = ArrayList(mutableListOf(car) + (this.cars))
    }

    fun remove(car: NagelCar) {
        this.cars.remove(car)
    }

    companion object {
        const val AVERAGE_CAR_LENGTH = 4.5
    }
}