package pl.edu.agh.cs.kraksim.api.factory.nagel.assertObject

import org.assertj.core.api.Assertions.assertThat
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.NagelSimulation
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationEntity
import pl.edu.agh.cs.kraksim.trafficLight.application.strategy.SOTLLightPhaseStrategy
import pl.edu.agh.cs.kraksim.trafficLight.application.strategy.TurnBasedLightPhaseStrategy
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.AlgorithmType
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyEntity
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.SimulationStateEntity

class NagelSimulationAssert(
    private val simulation: NagelSimulation
) {
    fun assertMovementSimulationStrategy(strategyEntity: MovementSimulationStrategyEntity): NagelSimulationAssert {
        assertThat(strategyEntity.type).isEqualTo(MovementSimulationStrategyType.NAGEL_SCHRECKENBERG)
        assertThat(simulation.movementSimulationStrategy.maxVelocity).isEqualTo(strategyEntity.maxVelocity)
        return this
    }

    fun assertLightPhaseStrategies(simulationEntity: SimulationEntity): NagelSimulationAssert {
        simulationEntity.lightPhaseStrategies.forEach { strategyEntity ->
            val parsedStrategy = simulation.lightPhaseManager.strategies.keys.find { it.id == strategyEntity.id }
            assertThat(parsedStrategy).isNotNull
            val intersectionList = simulation.lightPhaseManager.strategies[parsedStrategy]
            assertThat(intersectionList).isNotNull
            assertThat(strategyEntity.intersections.size).isEqualTo(intersectionList?.size)
            strategyEntity.intersections.forEach { assertThat(it).isIn(intersectionList) }
            when (strategyEntity.algorithm) {
                AlgorithmType.TURN_BASED -> assertThat(parsedStrategy is TurnBasedLightPhaseStrategy).isTrue
                AlgorithmType.SOTL -> assertThat(parsedStrategy is SOTLLightPhaseStrategy).isTrue
            }
        }
        return this
    }

    fun assertExpectedVelocities(simulationEntity: SimulationEntity): NagelSimulationAssert {
        simulation.statisticsManager.expectedVelocity.entries
            .forEach { assertThat(it.value).isEqualTo(simulationEntity.expectedVelocity[it.key]) }
        return this
    }

    fun assertGateways(simulationStateEntity: SimulationStateEntity): NagelSimulationAssert {
        simulationStateEntity.gatewaysStates.forEach { gatewayStateEntity ->
            val parsedGateway = simulation.state.gateways[gatewayStateEntity.gatewayId]
            assertThat(parsedGateway).isNotNull
            parsedGateway!!.generators.forEach { generator ->
                val generatorEntity = gatewayStateEntity.generators.find { it.id == generator.id }
                assertThat(generatorEntity).isNotNull
                assertThat(generator.targetGatewayId).isEqualTo(generatorEntity!!.targetGatewayId)
                assertThat(generator.gpsType).isEqualTo(generatorEntity.gpsType)
                assertThat(generator.lastCarReleasedTurnsAgo).isEqualTo(generatorEntity.lastCarReleasedTurnsAgo)
                assertThat(generator.carsToRelease).isEqualTo(generatorEntity.carsToRelease)
            }
        }
        return this
    }
}
