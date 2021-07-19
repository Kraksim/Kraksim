package pl.edu.agh.cs.kraksim.repository.entities

import javax.persistence.*

@Entity
class SimulationEntity(
    @OneToOne
    var mapEntity: MapEntity?,
    @OneToMany(cascade = [CascadeType.ALL])
    var trafficStateEntity: List<TrafficStateEntity>,
    var simulationType: SimulationType?
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class SimulationType {
    NAGEL_CORE
}
