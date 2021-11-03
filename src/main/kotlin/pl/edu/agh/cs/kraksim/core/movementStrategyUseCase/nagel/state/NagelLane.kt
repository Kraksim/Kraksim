package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state

import pl.edu.agh.cs.kraksim.common.Direction
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

    fun getNeighbouringLane(direction: Direction): NagelLane? {
        return parentRoad.getLane(indexFromLeft + direction.delta)
    }

    fun isSpaceOccupied(positionRelativeToStart: Int): Boolean {
        return cars.any { car -> car.positionRelativeToStart == positionRelativeToStart }
    }

    fun getCarsBeforeAndAfterPosition(positionRelativeToStart: Int): Pair<NagelCar?, NagelCar?> {
        require(!isSpaceOccupied(positionRelativeToStart))

        if (cars.isEmpty()) {
            return Pair(null, null)
        }

        if (cars[0].positionRelativeToStart > positionRelativeToStart) {
            return Pair(null, cars[0])
        }

        cars.forEachIndexed { i, car ->
            if (car.positionRelativeToStart > positionRelativeToStart) {
                val carBefore = cars[i - 1]
                return Pair(carBefore, car)
            }
        }
        return Pair(cars.last(), null)
    }

    fun getFreeSpaceInFrontOf(car: NagelCar): Int {
        val currentIndex = cars.indexOf(car)
        val carInFront = cars.getOrNull(currentIndex + 1)
        return carInFront?.distanceFrom(car) ?: car.distanceFromEndOfLane
    }

    fun getSpaceToEndFrom(position: Int): Int {
        return cellsCount - position - 1
    }

    fun doesLaneEndBeforeEndNode(): Boolean {
        return physicalEndingPoint != parentRoad.physicalLength
    }

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

    fun insertCarAt(positionRelativeToStart: Int, toAdd: NagelCar) {
        if (cars.isEmpty()) {
            cars.add(toAdd)
            return
        }

        val index: Int = cars.asSequence().mapIndexed { index, car -> Pair(index, car.positionRelativeToStart) }
            .filter { (_, position) -> position > positionRelativeToStart }
            .map { (index, _) -> index }
            .firstOrNull() ?: cars.size

        cars.add(index, toAdd)
    }
}
