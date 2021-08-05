package pl.edu.agh.cs.kraksim.repository.entities.trafficState

import org.springframework.lang.Nullable
import pl.edu.agh.cs.kraksim.common.CarId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.gps.GPSType
import pl.edu.agh.cs.kraksim.repository.LongArrayToStringConverter
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
    var gps: GPSEntity
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class GPSEntity(
    @Column
    @Convert(converter = LongArrayToStringConverter::class)
    var route: List<RoadId>,
    var type: GPSType

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}
