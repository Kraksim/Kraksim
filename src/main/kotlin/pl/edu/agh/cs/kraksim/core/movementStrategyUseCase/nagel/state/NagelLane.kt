package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state

import pl.edu.agh.cs.kraksim.common.addToFront
import pl.edu.agh.cs.kraksim.core.state.Car
import pl.edu.agh.cs.kraksim.core.state.Lane

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
    val cellsCount: Int = physicalLength / Car.AVERAGE_CAR_LENGTH

    override fun addCar(car: Car) {
        require(cars.isEmpty() || car.positionRelativeToStart < cars[0].positionRelativeToStart) { "Added car must be first in lane" }
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
}
