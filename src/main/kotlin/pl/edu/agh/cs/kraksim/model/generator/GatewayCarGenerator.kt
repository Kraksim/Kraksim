package pl.edu.agh.cs.kraksim.model.generator

import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.SimulationState

interface GatewayCarGenerator {
    fun generate(state: SimulationState)
}
