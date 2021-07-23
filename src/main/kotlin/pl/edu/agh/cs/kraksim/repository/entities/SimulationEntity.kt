package pl.edu.agh.cs.kraksim.repository.entities

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.LightPhaseStrategyEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.MovementSimulationStrategyEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.TrafficStateEntity
import javax.persistence.*

@Entity
class SimulationEntity(
        @OneToOne
    var mapEntity: MapEntity,
        @OneToMany(cascade = [CascadeType.ALL])
    var trafficStateEntities: List<TrafficStateEntity>,
        @OneToOne
    var movementSimulationStrategy: MovementSimulationStrategyEntity,
        var simulationType: SimulationType,
        @ElementCollection
    var expectedVelocity: Map<RoadId, Velocity>,
        @OneToMany
        var lightPhaseStrategies: List<LightPhaseStrategyEntity>
) {
    val latestTrafficStateEntity
        get() = trafficStateEntities.maxByOrNull { it.turn }!!

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class SimulationType {
    NAGEL_CORE
}
