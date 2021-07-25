package pl.edu.agh.cs.kraksim.repository.entities

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.LightPhaseStrategyEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.MovementSimulationStrategyEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.SimulationStateEntity
import javax.persistence.*

@Entity
class SimulationEntity(
    @OneToOne
    var mapEntity: MapEntity,
    //todo maybe we can somehow eager load only the last one?
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var simulationStateEntities: MutableList<SimulationStateEntity>,
    @OneToOne(cascade = [CascadeType.ALL])
    var movementSimulationStrategy: MovementSimulationStrategyEntity,
    var simulationType: SimulationType,
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    var expectedVelocity: Map<RoadId, Velocity>,
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    var lightPhaseStrategies: List<LightPhaseStrategyEntity>
) {
    val latestTrafficStateEntity
        get() = simulationStateEntities.maxByOrNull { it.turn }!!

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class SimulationType {
    NAGEL_CORE
}
