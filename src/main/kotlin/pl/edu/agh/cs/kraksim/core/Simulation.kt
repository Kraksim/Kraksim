package pl.edu.agh.cs.kraksim.core

import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.generator.GatewayCarGenerator
import pl.edu.agh.cs.kraksim.statistics.application.StatisticsManager
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseManager

interface Simulation {
    val state: SimulationState
    val movementSimulationStrategy: MovementSimulationStrategy
    val lightPhaseManager: LightPhaseManager
    val statisticsManager: StatisticsManager
    val gatewayCarGenerator: GatewayCarGenerator

    fun step() {
        gatewayCarGenerator.generate(state)
        movementSimulationStrategy.step(state)
        lightPhaseManager.changeLights()
        statisticsManager.createStatistics(state)
    }
}
