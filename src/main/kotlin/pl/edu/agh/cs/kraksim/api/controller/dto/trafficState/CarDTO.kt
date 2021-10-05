package pl.edu.agh.cs.kraksim.api.controller.dto.trafficState

import pl.edu.agh.cs.kraksim.common.CarId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.model.gps.GPSType

class CarDTO(
    var carId: CarId,
    var velocity: Velocity, // todo x5
    var currentLaneId: LaneId?,
    var positionRelativeToStart: Int,
    var gps: GPSDTO
) {
    var id: Long = 0
}

class GPSDTO(
    var route: List<RoadId>,
    var type: GPSType

) {
    var id: Long = 0
}
