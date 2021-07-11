package pl.edu.agh.cs.kraksim.repository.entities

import javax.persistence.*

@Entity
class SimulationEntity(
    @OneToOne
    var mapEntity: MapEntity,
    @OneToOne
    var trafficStateEntity: TrafficStateEntity,
    var stateType: StateType
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class StateType {
    NAGEL_SCHRECKENBERG
}