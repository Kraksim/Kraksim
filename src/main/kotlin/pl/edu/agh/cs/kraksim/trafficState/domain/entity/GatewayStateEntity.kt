package pl.edu.agh.cs.kraksim.trafficState.domain.entity

import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.gps.GPSType
import javax.persistence.*

@Entity
class GatewayStateEntity(
    var gatewayId: GatewayId,
    @OneToMany(cascade = [CascadeType.ALL])
    var collectedCars: List<CarEntity>,
    @OneToMany(cascade = [CascadeType.ALL])
    var generators: List<GeneratorEntity>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class GeneratorEntity(
    var lastCarReleasedTurnsAgo: Int,
    var releaseDelay: Int,
    var carsToRelease: Int,
    var targetGatewayId: GatewayId,
    var gpsType: GPSType

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}
