package pl.edu.agh.cs.kraksim.trafficState.domain.entity

import javax.persistence.*

@Entity
class SimulationStateEntity(
    var turn: Long = 0,
    @OneToMany(cascade = [CascadeType.ALL])
    var carsOnMap: List<CarEntity>,
    @OneToMany(cascade = [CascadeType.ALL])
    var trafficLights: List<TrafficLightEntity>,
    @OneToMany(cascade = [CascadeType.ALL])
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
