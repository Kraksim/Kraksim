package pl.edu.agh.cs.kraksim.repository.entities

import pl.edu.agh.cs.kraksim.common.*
import pl.edu.agh.cs.kraksim.repository.LongArrayToStringConverter
import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase
import javax.persistence.*

@Entity
class TrafficStateEntity(
    var turn: Long = 0,
    @ElementCollection
    var expectedVelocity: Map<RoadId, Velocity>,
    @OneToOne
    var movementSimulationStrategy: MovementSimulationStrategyEntity,
    @OneToMany
    var lightPhaseStrategies: List<LightPhaseStrategyEntity>,
    @OneToMany
    var carsOnMap: List<CarEntity>,
    @OneToMany
    var trafficLights: List<TrafficLightEntity>,
    @OneToMany
    var gatewaysStates: List<GatewayStateEntity>,
    var stateType: StateType
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class StateType {
    NAGEL_SCHRECKENBERG
}

@Entity
class TrafficLightEntity(
    var intersectionId: IntersectionId,
    @OneToMany
    var phases: List<PhaseEntity>,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class PhaseEntity(
    @Id
    var laneId: LaneId,
    var phaseTime: Int,
    var state: TrafficLightPhase.LightColor
)

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

enum class GPSType {
    DIJKSTRA_ROAD_LENGTH
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

@Entity
class CarEntity(
    var velocity: Velocity,
    var currentLaneId: LaneId,
    var positionRelativeToStart: Int,
    @OneToOne
    var gps: GPSEntity,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class MovementSimulationStrategyEntity(
    var type: MovementSimulationStrategyType,
    var randomProvider: RandomProviderType,
    var slowDownProbability: Double,
    var maxVelocity: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class MovementSimulationStrategyType {
    NAGEL_SCHRECKENBERG
}

enum class RandomProviderType {
    TRUE
}

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
