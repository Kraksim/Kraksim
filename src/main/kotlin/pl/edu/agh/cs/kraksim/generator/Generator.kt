package pl.edu.agh.cs.kraksim.generator

import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.gps.GPSType

data class Generator(
    /**
     * Value how many turns ago car was released
     */
    var lastCarReleasedTurnsAgo: Int,
    /**
     * Delay between turns in which we release new car,
     * value = 0 means each turn car is released
     */
    val releaseDelay: Int,
    var carsToRelease: Int,
    val targetGatewayId: GatewayId,
    val gpsType: GPSType,
    val id: Long = 0
)
