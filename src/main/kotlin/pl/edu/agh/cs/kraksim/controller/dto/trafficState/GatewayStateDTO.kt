package pl.edu.agh.cs.kraksim.controller.dto.trafficState

import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.gps.GPSType

class GatewayStateDTO(
    var gatewayId: GatewayId,
    var collectedCars: List<CarDTO>,
    var generators: List<GeneratorDTO>
) {
    var id: Long = 0
}

class GeneratorDTO(
    var carsToRelease: Int,
    var releaseDelay: Int,
    var targetGatewayId: GatewayId,
    var gpsType: GPSType

) {
    var id: Long = 0
}
