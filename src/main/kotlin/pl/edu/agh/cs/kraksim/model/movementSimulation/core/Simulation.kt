package pl.edu.agh.cs.kraksim.model.movementSimulation.core

import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.SimulationState
import pl.edu.agh.cs.kraksim.model.generator.GatewayCarGenerator
import pl.edu.agh.cs.kraksim.model.statistics.StatisticsManager
import pl.edu.agh.cs.kraksim.model.trafficLight.LightPhaseManager

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
