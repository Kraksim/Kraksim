package pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.state

import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.Car
import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.Lane
import pl.edu.agh.cs.kraksim.model.gps.GPS
import kotlin.math.abs

class NagelCar(
    override val id: Long = 0,
    override var velocity: Int = 0,
    override val gps: GPS,
) : Car {
    override var positionRelativeToStart: Int = 0
    var currentLane: NagelLane? = null
    var distanceLeftToMove: Int = 0

    val distanceFromRoadNode: Int
        get() = currentLane!!.cellsCount - positionRelativeToStart - 1

    override fun moveToLane(lane: Lane?, newPosition: Int) {
        currentLane?.remove(this)
        currentLane = lane as NagelLane?
        positionRelativeToStart = newPosition
        lane?.addCar(this)
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
