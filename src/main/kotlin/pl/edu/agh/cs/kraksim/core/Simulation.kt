package pl.edu.agh.cs.kraksim.core

import pl.edu.agh.cs.kraksim.nagelCore.simulation.LightPhaseManager

interface Simulation {
    val state: SimulationState
    val movementSimulationStrategy: MovementSimulationStrategy
    val lightPhaseManager: LightPhaseManager

    fun step()
}
