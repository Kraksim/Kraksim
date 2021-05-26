package pl.edu.agh.cs.kraksim.core

import pl.edu.agh.cs.kraksim.nagelCore.NagelGateway
import pl.edu.agh.cs.kraksim.nagelCore.NagelSimulationState

class Simulation(
    var state: SimulationState,
) {


    fun xd() {
        val simulationState: SimulationState = NagelSimulationState(ArrayList(), ArrayList(), ArrayList())
        simulationState.gateways = ArrayList<NagelGateway>()
    }

}