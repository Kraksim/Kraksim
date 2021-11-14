package pl.edu.agh.cs.kraksim.trafficState.domain.entity

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.common.converter.LongArrayToStringConverter
import pl.edu.agh.cs.kraksim.trafficLight.domain.TrafficLightPhase
import javax.persistence.*

@Entity
class TrafficLightEntity(
    var intersectionId: IntersectionId,
    @OneToMany(cascade = [CascadeType.ALL])
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
    var period: Int?,
    var state: TrafficLightPhase.LightColor
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class LightPhaseStrategyEntity(
    var algorithm: AlgorithmType,
    var turnLength: Int? = null,
    val phiFactor: Double? = null,
    val minPhaseLength: Int? = null,
    @Convert(converter = LongArrayToStringConverter::class)
    var intersections: List<IntersectionId>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class AlgorithmType {
    TURN_BASED, SOTL
}
