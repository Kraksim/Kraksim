package pl.edu.agh.cs.kraksim.model.movementSimulation.core

import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.SimulationState

interface MovementSimulationStrategy {
    fun step(state: SimulationState)
}
