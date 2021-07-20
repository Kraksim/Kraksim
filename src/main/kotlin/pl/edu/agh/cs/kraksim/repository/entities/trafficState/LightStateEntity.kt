package pl.edu.agh.cs.kraksim.repository.entities.trafficState

import pl.edu.agh.cs.kraksim.common.IntersectionId
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.repository.LongArrayToStringConverter
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase
import javax.persistence.*

@Entity
class TrafficLightEntity(
    var intersectionId: IntersectionId,
    @OneToMany
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
