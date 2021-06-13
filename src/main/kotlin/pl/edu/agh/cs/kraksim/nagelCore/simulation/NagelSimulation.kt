package pl.edu.agh.cs.kraksim.nagelCore.simulation

import pl.edu.agh.cs.kraksim.core.Simulation
import pl.edu.agh.cs.kraksim.nagelCore.NagelSimulationState

class NagelSimulation(
    override val state: NagelSimulationState,
    override var movementSimulationStrategy: NagelMovementSimulationStrategy,
    override var lightPhaseManager: LightPhaseManager
) : Simulation {

    override fun step() {
        movementSimulationStrategy.step(state)
    }
}
