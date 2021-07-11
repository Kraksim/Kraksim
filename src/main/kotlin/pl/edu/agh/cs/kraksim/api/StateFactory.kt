package pl.edu.agh.cs.kraksim.api

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.api.nagel.NagelSimulationStateFactory
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity
import pl.edu.agh.cs.kraksim.repository.entities.StateType

@Component
class StateFactory(
    val nagelSimulationStateFactory: NagelSimulationStateFactory
) {

    fun from(
        entity: SimulationEntity
    ): SimulationState =
        when (entity.stateType) {
            StateType.NAGEL_SCHRECKENBERG -> nagelSimulationStateFactory.from(entity)
        }
}
