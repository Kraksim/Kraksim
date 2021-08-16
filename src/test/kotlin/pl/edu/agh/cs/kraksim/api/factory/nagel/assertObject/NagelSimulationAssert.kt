package pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject

import org.assertj.core.api.Assertions.assertThat
import pl.edu.agh.cs.kraksim.nagelCore.NagelSimulation
import pl.edu.agh.cs.kraksim.repository.entities.MapEntity
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.MovementSimulationStrategyEntity
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.SimulationStateEntity

class NagelSimulationAssert(
        private val simulation: NagelSimulation
) {
    fun assertMovementSimulationStrategy(strategyEntity: MovementSimulationStrategyEntity){
        assertThat(strategyEntity.type).isEqualTo(MovementSimulationStrategyType.NAGEL_SCHRECKENBERG)
        assertThat(simulation.movementSimulationStrategy.maxVelocity).isEqualTo(strategyEntity.maxVelocity)
    }

    fun assertLightPhaseStrategies(simulationEntity: SimulationEntity){
        simulationEntity.lightPhaseStrategies.forEach{ lightPhaseStrategyEntity ->
            val parsedLightPhaseStrategy = simulation.lightPhaseManager.strategies.forEach{ }
        }
    }
}
