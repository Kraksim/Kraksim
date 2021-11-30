package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state

import pl.edu.agh.cs.kraksim.common.Direction
import pl.edu.agh.cs.kraksim.core.state.Car
import pl.edu.agh.cs.kraksim.core.state.Lane
import pl.edu.agh.cs.kraksim.gps.GPS
import kotlin.math.abs

class NagelCar(
    override val id: Long = 0,
    override var velocity: Int = 0,
    override val gps: GPS,
    var brakeLightOn: Boolean? = null
) : Car {
    override var positionRelativeToStart: Int = 0
    var currentLane: NagelLane? = null
    var distanceLeftToMove: Int = 0

    val distanceFromEndOfLane: Int
        get() {
            val result = currentLane!!.cellsCount - positionRelativeToStart - 1
            if (result < 0) throw IllegalStateException("Car position is greater than lane length, carId=$id, position=$positionRelativeToStart, currentLaneId=${currentLane?.id}")
            return result
        }

    override fun moveToLaneFront(lane: Lane?, newPosition: Int) {
        currentLane?.remove(this)
        currentLane = lane as NagelLane?
        positionRelativeToStart = newPosition
        lane?.addCar(this)
        distanceLeftToMove = 0
    }

    fun moveToLane(lane: Lane, newPosition: Int) {
        currentLane?.remove(this)
        currentLane = lane as NagelLane?
        positionRelativeToStart = newPosition
        lane.insertCarAt(newPosition, this)
        distanceLeftToMove = 0
    }

    fun moveForward(distance: Int) {
        positionRelativeToStart += distance
        distanceLeftToMove = velocity - distance
    }

    fun hasDistanceLeftToMove(): Boolean {
        return distanceLeftToMove > 0
    }

    fun distanceFrom(car: NagelCar): Int {
        return abs(positionRelativeToStart - car.positionRelativeToStart) - 1
    }

    override fun toString(): String {
        return "NagelCar(positionRelativeToStart=$positionRelativeToStart, velocity=$velocity, distanceLeftToMove=$distanceLeftToMove)"
    }

    fun getChangeLaneDirection(): Direction {
        return gps.getChangeLaneDirection(currentLane!!)
    }
}
