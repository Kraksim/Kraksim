package pl.edu.agh.cs.kraksim.repository.entities.trafficState

import pl.edu.agh.cs.kraksim.common.GatewayId
import javax.persistence.*

@Entity
class GatewayStateEntity(
    var gatewayId: GatewayId,
    @OneToMany
    var collectedCars: List<CarEntity>,
    @OneToMany
    var generators: List<GeneratorEntity>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class GeneratorEntity(
    var carsToRelease: Int,
    var releaseDelay: Int,
    var targetGatewayId: GatewayId,
    var gpsType: GPSType

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class GPSType {
    DIJKSTRA_ROAD_LENGTH
}
