package pl.edu.agh.cs.kraksim.core

import pl.edu.agh.cs.kraksim.core.state.SimulationState

interface MovementSimulationStrategy {
    fun step(state: SimulationState)
}
