package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.core.Simulation
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseManager

class NagelSimulation(
    override val state: NagelSimulationState,
    override var movementSimulationStrategy: NagelMovementSimulationStrategy,
    override var lightPhaseManager: LightPhaseManager
) : Simulation {

    override fun step() {
        movementSimulationStrategy.step(state)
        lightPhaseManager.changeLights()
    }
}
