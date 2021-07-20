package pl.edu.agh.cs.kraksim.repository.entities.trafficState

import pl.edu.agh.cs.kraksim.common.CarId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.repository.LongArrayToStringConverter
import javax.persistence.*

@Entity
class CarEntity(
    var carId: CarId,
    var velocity: Velocity,
    var currentLaneId: LaneId,
    var positionRelativeToStart: Int,
    @OneToOne
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
