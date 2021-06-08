package pl.edu.agh.cs.kraksim.core

interface Simulation {
    val state: SimulationState

    fun step()
}
