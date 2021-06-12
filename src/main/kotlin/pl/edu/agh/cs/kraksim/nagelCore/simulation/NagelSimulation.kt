package pl.edu.agh.cs.kraksim.nagelCore.simulation

import pl.edu.agh.cs.kraksim.core.Simulation
import pl.edu.agh.cs.kraksim.nagelCore.NagelSimulationState

class NagelSimulation(
    override val state: NagelSimulationState,
    var nagelMovementSimulationStrategy: NagelMovementSimulationStrategy
//    var lightPhaseManager: LightPhaseManager
) : Simulation {

    override fun step() {
        nagelMovementSimulationStrategy.step(state)
    }
}
