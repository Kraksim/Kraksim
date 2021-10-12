package pl.edu.agh.cs.kraksim.api.factory

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.random.TrueRandomProvider
import pl.edu.agh.cs.kraksim.nagelCore.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.trafficState.domain.MovementSimulationStrategyEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.trafficState.domain.RandomProviderType

@Component
class MovementSimulationStrategyFactory {
    fun from(entity: MovementSimulationStrategyEntity) = when (entity.type) {
        MovementSimulationStrategyType.NAGEL_SCHRECKENBERG -> createNagelStrategy(entity)
    }

    private fun createNagelStrategy(entity: MovementSimulationStrategyEntity): NagelMovementSimulationStrategy {
        val randomProvider = createRandomProvider(entity)
        return NagelMovementSimulationStrategy(
            randomProvider,
            entity.maxVelocity
        )
    }

    private fun createRandomProvider(entity: MovementSimulationStrategyEntity) = when (entity.randomProvider) {
        RandomProviderType.TRUE -> TrueRandomProvider(entity.slowDownProbability)
    }
}
