package pl.edu.agh.cs.kraksim.simulation.application

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.core.MovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.Simulation
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.generator.GatewayCarGenerator
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.NagelSimulation
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationType
import pl.edu.agh.cs.kraksim.statistics.application.StatisticsManager
import pl.edu.agh.cs.kraksim.trafficLight.application.LightPhaseManager

@Component
class SimulationFactory(
    val gatewayCarGenerator: GatewayCarGenerator
) {

    fun from(
        simulationType: SimulationType,
        simulationState: SimulationState,
        movementStrategy: MovementSimulationStrategy,
        lightPhaseManager: LightPhaseManager,
        statisticsManager: StatisticsManager,
    ): Simulation = when (simulationType) {
        SimulationType.NAGEL_CORE -> createNagel(
            simulationState,
            movementStrategy,
            lightPhaseManager,
            statisticsManager
        )
    }

    private fun createNagel(
        simulationState: SimulationState,
        movementStrategy: MovementSimulationStrategy,
        lightPhaseManager: LightPhaseManager,
        statisticsManager: StatisticsManager,
    ): NagelSimulation {
        require(simulationState is NagelSimulationState) { "Error creating NagelSimulation - simulation state expected to be NagelSimulationState, but was $simulationState" }
        require(movementStrategy is NagelMovementSimulationStrategy) { "Error creating NagelSimulation - movement strategy expected to be NagelMovementSimulationStrategy, but was $movementStrategy" }

        return NagelSimulation(
            simulationState, movementStrategy, lightPhaseManager, statisticsManager, gatewayCarGenerator
        )
    }
}
