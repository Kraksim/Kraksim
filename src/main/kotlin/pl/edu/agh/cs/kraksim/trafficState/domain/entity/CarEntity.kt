package pl.edu.agh.cs.kraksim.trafficState.domain.entity

import org.springframework.lang.Nullable
import pl.edu.agh.cs.kraksim.common.CarId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.common.converter.LongArrayToStringConverter
import pl.edu.agh.cs.kraksim.gps.GPSType
import javax.persistence.*
import kotlin.random.Random

@Entity
class CarEntity(
    var carId: CarId = Random.nextLong(),
    var velocity: Velocity,
    @Nullable
    var currentLaneId: LaneId?,
    var positionRelativeToStart: Int,
    @OneToOne(cascade = [CascadeType.ALL])
    var gps: GPSEntity,
    var brakeLightOn: Boolean? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class GPSEntity(
    @Column(length = 10485760)
    @Convert(converter = LongArrayToStringConverter::class)
    var route: List<RoadId>,
    var type: GPSType

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}
