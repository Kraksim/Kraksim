package pl.edu.agh.cs.kraksim.repository.entities.trafficState

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity
import javax.persistence.*

@Entity
class TrafficStateEntity(
    var turn: Long = 0,
    @OneToMany(cascade = [CascadeType.ALL])
    var carsOnMap: List<CarEntity>,
    @OneToMany
    var trafficLights: List<TrafficLightEntity>,
    @OneToMany
    var gatewaysStates: List<GatewayStateEntity>,
    @ManyToOne
    var simulation: SimulationEntity,
    var stateType: StateType
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class StateType {
    NAGEL_SCHRECKENBERG
}
