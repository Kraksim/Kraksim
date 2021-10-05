package pl.edu.agh.cs.kraksim.model.factory

import org.springframework.stereotype.Component
import pl.edu.agh.cs.kraksim.model.movementSimulation.core.MovementSimulationStrategy
import pl.edu.agh.cs.kraksim.model.movementSimulation.core.Simulation
import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.SimulationState
import pl.edu.agh.cs.kraksim.model.generator.GatewayCarGenerator
import pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.NagelSimulation
import pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.repository.entities.SimulationType
import pl.edu.agh.cs.kraksim.model.statistics.StatisticsManager
import pl.edu.agh.cs.kraksim.model.trafficLight.LightPhaseManager

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
