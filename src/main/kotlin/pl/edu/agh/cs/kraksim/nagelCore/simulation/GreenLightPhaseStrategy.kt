package pl.edu.agh.cs.kraksim.nagelCore.simulation

import pl.edu.agh.cs.kraksim.comon.TrafficLightPhase
import pl.edu.agh.cs.kraksim.nagelCore.NagelSimulationState

class GreenLightPhaseStrategy {

    fun getNext(nagelSimulationState: NagelSimulationState): TrafficLightPhase.LightColor {
        return TrafficLightPhase.LightColor.GREEN
    }
}