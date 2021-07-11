package pl.edu.agh.cs.kraksim.repository.entities

import pl.edu.agh.cs.kraksim.common.*
import pl.edu.agh.cs.kraksim.repository.LongArrayToStringConverter
import javax.persistence.*

@Entity
class TrafficStateEntity(
    var turn: Long = 0,
    @ElementCollection
    var expectedVelocity: Map<RoadId, Velocity>,
    @OneToOne
    var movementSimulationStrategy: MovementSimulationStrategyEntity,
    @OneToMany
    var cars: List<CarEntity>,
    @OneToMany
    var trafficLights: List<TrafficLightEntity>,
    @OneToMany
    var gatewaysStates: List<GatewayStateEntity>,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class TrafficLightEntity(
    var intersectionId: IntersectionId,
    @OneToOne
    var lightPhaseStrategy: LightPhaseStrategyEntity,
    @ElementCollection
    var phases: Map<RoadId, Velocity>,
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
    var state: TrafficLightState
)

enum class TrafficLightState {
    RED, GREEN
}

@Entity
class LightPhaseStrategyEntity(
    var algorithm: AlgorithmType,
    var turnLength: Int
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
    var currentRoadID: RoadId,
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
