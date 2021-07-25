package pl.edu.agh.cs.kraksim.api.factory

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.api.factory.nagel.NagelSimulationStateFactory
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.StateType
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.SimulationStateEntity

@Component
class StateFactory(
    val nagelSimulationStateFactory: NagelSimulationStateFactory
) {
    fun toEntity(
        simulationState: SimulationState,
        simulationEntity: SimulationEntity
    ): SimulationStateEntity = when (simulationState) {
        is NagelSimulationState -> nagelSimulationStateFactory.toEntity(simulationState, simulationEntity)
        else -> throw Error("Expected Nagel simulation state")
    }

    fun from(
        entity: SimulationEntity
    ): SimulationState =
        when (entity.latestTrafficStateEntity.stateType) {
            StateType.NAGEL_SCHRECKENBERG -> nagelSimulationStateFactory.from(entity)
        }
}
