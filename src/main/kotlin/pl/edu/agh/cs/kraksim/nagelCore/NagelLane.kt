package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.comon.addToFront
import kotlin.math.roundToInt

class NagelLane(
    private val id: Long,
    val indexFromLeft: Int,
    val parentRoad: NagelRoad,
    private val physicalStartingPoint: Int,
    private val physicalEndingPoint: Int,
) {
    // lista ma same auta, nie ma pustych komórek. Posortowana po pozycji bo chcemy miec łatwy odstep do auta przed danym autem
    val cars: MutableList<NagelCar> = ArrayList()

    val physicalLength: Int = physicalEndingPoint - physicalStartingPoint
    val cellsCount: Int = (physicalLength / AVERAGE_CAR_LENGTH).roundToInt()

    fun addCar(car: NagelCar) {
        this.cars.addToFront(car)
    }

    fun remove(car: NagelCar) {
        this.cars.remove(car)
    }

    fun containsCar(): Boolean = cars.isNotEmpty()

    fun getFreeSpaceInFront(): Int {
        val positionOfFirstCar: Int? = cars.getOrNull(0)?.positionRelativeToStart
        return positionOfFirstCar ?: cellsCount
    }

    override fun toString(): String {
        return "NagelLane(id=$id, \n\t\tcars=$cars\n)"
    }

    companion object {
        const val AVERAGE_CAR_LENGTH = 4.5
    }

}
