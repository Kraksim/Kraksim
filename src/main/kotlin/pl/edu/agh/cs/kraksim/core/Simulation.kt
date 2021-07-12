package pl.edu.agh.cs.kraksim.core

import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.statistics.StatisticsManager
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseManager

interface Simulation {
    val state: SimulationState
    val movementSimulationStrategy: MovementSimulationStrategy
    val lightPhaseManager: LightPhaseManager
    val statisticsManager: StatisticsManager

    fun step()
}
