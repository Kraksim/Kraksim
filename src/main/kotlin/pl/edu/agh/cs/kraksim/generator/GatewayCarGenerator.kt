package pl.edu.agh.cs.kraksim.generator

import pl.edu.agh.cs.kraksim.core.state.SimulationState

interface GatewayCarGenerator {
    fun generate(state: SimulationState)
}
