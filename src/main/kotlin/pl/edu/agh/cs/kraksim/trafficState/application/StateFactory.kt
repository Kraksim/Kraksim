package pl.edu.agh.cs.kraksim.trafficState.application

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.factory.NagelSimulationStateFactory
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.SimulationStateEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.StateType

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
