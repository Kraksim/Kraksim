package pl.edu.agh.cs.kraksim.generator.request

import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.gps.GPSType
import javax.validation.constraints.Positive

class CreateGeneratorRequest {
    @field:Positive
    var releaseDelay: Int = 0
    @field:Positive
    var carsToRelease: Int = 0
    var targetGatewayId: GatewayId = 0
    lateinit var gpsType: GPSType
}
