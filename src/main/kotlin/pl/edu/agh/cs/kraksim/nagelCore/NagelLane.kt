package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.common.addToFront
import pl.edu.agh.cs.kraksim.core.Car
import pl.edu.agh.cs.kraksim.core.Lane
import kotlin.math.roundToInt

class NagelLane(
    override val id: Long,
    override val indexFromLeft: Int,
    override val parentRoad: NagelRoad,
    override val physicalStartingPoint: Int,
    override val physicalEndingPoint: Int,
) : Lane {
    // lista ma same auta, nie ma pustych komórek. Posortowana po pozycji bo chcemy miec łatwy odstep do auta przed danym autem

    override val cars: MutableList<NagelCar> = ArrayList()

    override val physicalLength: Int = physicalEndingPoint - physicalStartingPoint
    val cellsCount: Int = (physicalLength / AVERAGE_CAR_LENGTH).roundToInt()

    override fun addCar(car: Car) {
        this.cars.addToFront(car as NagelCar)
    }

    override fun remove(car: Car) {
        this.cars.remove(car)
    }

    override fun containsCar(): Boolean = cars.isNotEmpty()

    override fun getFreeSpaceInFront(): Int {
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
