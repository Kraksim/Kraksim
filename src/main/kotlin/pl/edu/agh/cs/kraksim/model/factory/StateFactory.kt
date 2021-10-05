package pl.edu.agh.cs.kraksim.model.factory

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.model.factory.nagel.NagelSimulationStateFactory
import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.SimulationState
import pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.SimulationStateEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.StateType

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
