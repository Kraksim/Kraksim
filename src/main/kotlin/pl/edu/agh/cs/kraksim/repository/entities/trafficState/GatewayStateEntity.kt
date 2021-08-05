package pl.edu.agh.cs.kraksim.repository.entities.trafficState

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import pl.edu.agh.cs.kraksim.common.GatewayId
import pl.edu.agh.cs.kraksim.gps.GPSType
import javax.persistence.*

@Entity
class GatewayStateEntity(
    var gatewayId: GatewayId,
    @LazyCollection(value = LazyCollectionOption.FALSE)
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
    var carsToRelease: Int,
    var releaseDelay: Int,
    var targetGatewayId: GatewayId,
    var gpsType: GPSType

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}
