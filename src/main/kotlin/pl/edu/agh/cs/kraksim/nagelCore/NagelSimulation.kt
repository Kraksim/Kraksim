package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.core.Simulation
import pl.edu.agh.cs.kraksim.generator.GatewayCarGenerator
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.statistics.StatisticsManager
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseManager

class NagelSimulation(
    override val state: NagelSimulationState,
    override val movementSimulationStrategy: NagelMovementSimulationStrategy,
    override val lightPhaseManager: LightPhaseManager,
    override val statisticsManager: StatisticsManager,
    override val gatewayCarGenerator: GatewayCarGenerator
) : Simulation
