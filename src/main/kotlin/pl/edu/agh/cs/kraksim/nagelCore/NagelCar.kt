package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.core.Car
import kotlin.math.abs

class NagelCar(
    override var velocity: Int = 0,
):Car {
    override var positionRelativeToStart: Int = 0
    var currentLane: NagelLane? = null
    var distanceLeftToMove: Int = 0

    val distanceFromRoadNode: Int
        get() {
            return currentLane!!.cellsCount - positionRelativeToStart - 1
        }

    fun moveToLane(lane: NagelLane?, newPosition: Int = 0) {
        currentLane?.remove(this)
        currentLane = lane
        lane?.addCar(this)
        positionRelativeToStart = newPosition
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

}