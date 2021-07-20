package pl.edu.agh.cs.kraksim.repository.entities

import pl.edu.agh.cs.kraksim.repository.entities.trafficState.TrafficStateEntity
import javax.persistence.*

@Entity
class SimulationEntity(
    @OneToOne
    var mapEntity: MapEntity,
    @OneToMany(cascade = [CascadeType.ALL])
    var trafficStateEntities: List<TrafficStateEntity>,
    var simulationType: SimulationType
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
