package pl.edu.agh.cs.kraksim.trafficState.domain.dto

import pl.edu.agh.cs.kraksim.common.CarId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.gps.GPSType

class CarDTO(
    var carId: CarId,
    var velocity: Velocity,
    var currentLaneId: LaneId?,
    var positionRelativeToStart: Int,
    var gps: GPSDTO,
    var brakeLightOn: Boolean? = null
) {
    var id: Long = 0
}

class GPSDTO(
    var route: List<RoadId>,
    var type: GPSType

) {
    var id: Long = 0
}
