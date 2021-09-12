package pl.edu.agh.cs.kraksim.repository.entities.trafficState

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity
import javax.persistence.*

@Entity
class SimulationStateEntity(
    var turn: Long = 0,
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    var carsOnMap: List<CarEntity>,
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    var trafficLights: List<TrafficLightEntity>,
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    var gatewaysStates: List<GatewayStateEntity>,
    var stateType: StateType
) {
    constructor(
        turn: Long = 0,
        carsOnMap: List<CarEntity>,
        trafficLights: List<TrafficLightEntity>,
        gatewaysStates: List<GatewayStateEntity>,
        stateType: StateType,
        simulation: SimulationEntity
    ) : this(turn, carsOnMap, trafficLights, gatewaysStates, stateType) {
        this.simulation = simulation
    }

    @Suppress("JoinDeclarationAndAssignment")
    @ManyToOne
    lateinit var simulation: SimulationEntity

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class StateType {
    NAGEL_SCHRECKENBERG
}
