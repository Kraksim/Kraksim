package pl.edu.agh.cs.kraksim.core

interface MovementSimulationStrategy {
    fun step(state: SimulationState)
}
