package pl.edu.agh.cs.kraksim.api.factory

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.api.factory.nagel.NagelSimulationStateFactory
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.SimulationStateEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.StateType

@Component
class StateFactory(
    val nagelSimulationStateFactory: NagelSimulationStateFactory
) {
    fun toEntity(
        simulationState: SimulationState,
        simulationEntity: SimulationEntity
    ): SimulationStateEntity = when (simulationState.type) {
        StateType.NAGEL_SCHRECKENBERG -> nagelSimulationStateFactory.toEntity(simulationState as NagelSimulationState, simulationEntity)
    }

    fun from(
        entity: SimulationEntity
    ): SimulationState =
        when (entity.latestTrafficStateEntity.stateType) {
            StateType.NAGEL_SCHRECKENBERG -> nagelSimulationStateFactory.from(entity)
        }
}
