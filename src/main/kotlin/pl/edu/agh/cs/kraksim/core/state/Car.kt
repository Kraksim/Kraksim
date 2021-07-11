package pl.edu.agh.cs.kraksim.core.state

import pl.edu.agh.cs.kraksim.gps.GPS

interface Car {
    val id: Long
    var velocity: Int
    var positionRelativeToStart: Int
    val gps: GPS
}
