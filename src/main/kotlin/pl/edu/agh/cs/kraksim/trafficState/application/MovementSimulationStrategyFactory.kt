package pl.edu.agh.cs.kraksim.trafficState.application

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.common.random.TrueRandomProvider
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.brakeLight.BrakeLightMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.multilaneNagel.MultiLaneNagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.RandomProviderType

@Component
class MovementSimulationStrategyFactory {
    fun from(entity: MovementSimulationStrategyEntity) = when (entity.type) {
        MovementSimulationStrategyType.NAGEL_SCHRECKENBERG -> createNagelStrategy(entity)
        MovementSimulationStrategyType.MULTI_LANE_NAGEL_SCHRECKENBERG -> createMultiLaneNagelStrategy(entity)
        MovementSimulationStrategyType.BRAKE_LIGHT -> createBrakeLightStrategy(entity)
    }

    private fun createNagelStrategy(entity: MovementSimulationStrategyEntity): NagelMovementSimulationStrategy {
        val randomProvider = createRandomProvider(entity)
        return NagelMovementSimulationStrategy(
            randomProvider,
            entity.maxVelocity
        )
    }

    private fun createMultiLaneNagelStrategy(entity: MovementSimulationStrategyEntity): MultiLaneNagelMovementSimulationStrategy {
        val randomProvider = createRandomProvider(entity)
        return MultiLaneNagelMovementSimulationStrategy(
            randomProvider,
            entity.maxVelocity
        )
    }

    private fun createBrakeLightStrategy(entity: MovementSimulationStrategyEntity): BrakeLightMovementSimulationStrategy {
        requireNotNull(entity.threshold)
        val randomProvider = createRandomProvider(entity)
        return BrakeLightMovementSimulationStrategy(
            randomProvider,
            entity.maxVelocity,
            entity.threshold!!,
            breakLightReactionProbability = entity.breakLightReactionProbability!!,
            accelerationDelayProbability = entity.accelerationDelayProbability!!,
            defaultProbability = entity.slowDownProbability
        )
    }

    private fun createRandomProvider(entity: MovementSimulationStrategyEntity) = when (entity.randomProvider) {
        RandomProviderType.TRUE -> TrueRandomProvider(entity.slowDownProbability)
    }
}
