package pl.edu.agh.cs.kraksim.nagelCore.simulation

import pl.edu.agh.cs.kraksim.nagelCore.NagelSimulationState

class NagelSimulation(
    var state: NagelSimulationState,
    var nagelMovementSimulationStrategy: NagelMovementSimulationStrategy
//    var lightPhaseManager: LightPhaseManager
) {

    fun step() {
        nagelMovementSimulationStrategy.step(state)
    }


}