package pl.edu.agh.cs.kraksim.model.movementSimulation.nagel

import pl.edu.agh.cs.kraksim.model.movementSimulation.core.Simulation
import pl.edu.agh.cs.kraksim.model.generator.GatewayCarGenerator
import pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.model.statistics.StatisticsManager
import pl.edu.agh.cs.kraksim.model.trafficLight.LightPhaseManager

class NagelSimulation(
    override val state: NagelSimulationState,
    override val movementSimulationStrategy: NagelMovementSimulationStrategy,
    override val lightPhaseManager: LightPhaseManager,
    override val statisticsManager: StatisticsManager,
    override val gatewayCarGenerator: GatewayCarGenerator
) : Simulation
