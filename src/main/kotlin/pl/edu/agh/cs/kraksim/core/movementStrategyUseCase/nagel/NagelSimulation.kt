package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel

import pl.edu.agh.cs.kraksim.core.Simulation
import pl.edu.agh.cs.kraksim.generator.GatewayCarGenerator
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.statistics.application.StatisticsManager
import pl.edu.agh.cs.kraksim.trafficLight.application.LightPhaseManager

class NagelSimulation(
    override val state: NagelSimulationState,
    override val movementSimulationStrategy: NagelMovementSimulationStrategy,
    override val lightPhaseManager: LightPhaseManager,
    override val statisticsManager: StatisticsManager,
    override val gatewayCarGenerator: GatewayCarGenerator
) : Simulation
