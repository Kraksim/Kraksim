package pl.edu.agh.cs.kraksim.simulation.domain

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.statistics.domain.StatisticsEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.LightPhaseStrategyEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.SimulationStateEntity
import javax.persistence.*

@Entity
class SimulationEntity(
    @OneToOne
    var mapEntity: MapEntity,
    @OneToMany(cascade = [CascadeType.ALL])
    var simulationStateEntities: MutableList<SimulationStateEntity>,
    @OneToOne(cascade = [CascadeType.ALL])
    var movementSimulationStrategy: MovementSimulationStrategyEntity,
    var simulationType: SimulationType,
    @ElementCollection
    var expectedVelocity: Map<RoadId, Velocity>,
    @OneToMany(cascade = [CascadeType.ALL])
    var lightPhaseStrategies: List<LightPhaseStrategyEntity>,
    @OneToMany(cascade = [CascadeType.ALL])
    var statisticsEntities: List<StatisticsEntity>,
    var name: String
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
