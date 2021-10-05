package pl.edu.agh.cs.kraksim.model.movementSimulation.core.state

import pl.edu.agh.cs.kraksim.model.gps.GPS

interface Car {
    val id: Long
    var velocity: Int
    var positionRelativeToStart: Int
    val gps: GPS

    fun moveToLane(lane: Lane?, newPosition: Int = 0)

    companion object {
        const val AVERAGE_CAR_LENGTH = 5
    }
}
