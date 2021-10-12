package pl.edu.agh.cs.kraksim.trafficState.domain

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.converter.LongArrayToStringConverter
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase
import javax.persistence.*

@Entity
class TrafficLightEntity(
    var intersectionId: IntersectionId,
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(value = LazyCollectionOption.FALSE)
    var phases: List<PhaseEntity>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class PhaseEntity(
    var laneId: LaneId,
    var phaseTime: Int,
    var state: TrafficLightPhase.LightColor
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class LightPhaseStrategyEntity(
    var algorithm: AlgorithmType,
    var turnLength: Int,
    @Convert(converter = LongArrayToStringConverter::class)
    var intersections: List<IntersectionId>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class AlgorithmType {
    TURN_BASED
}
