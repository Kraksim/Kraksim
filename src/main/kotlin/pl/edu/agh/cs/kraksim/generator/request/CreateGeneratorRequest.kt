package pl.edu.agh.cs.kraksim.generator.request

import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.gps.GPSType

class CreateGeneratorRequest {
    var releaseDelay: Int = 0
    var carsToRelease: Int = 0
    var targetGatewayId: GatewayId = 0
    lateinit var gpsType: GPSType
}
