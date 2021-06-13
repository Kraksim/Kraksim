package pl.edu.agh.cs.kraksim.nagelCore.simulation

import pl.edu.agh.cs.kraksim.nagelCore.NagelSimulationState

class LightPhaseManager(
    val lightStrategies: HashMap<Long, GreenLightPhaseStrategy> = HashMap()
) {

    fun changeLights(nagelSimulationState: NagelSimulationState) {
        nagelSimulationState.intersections // todo przemyslec czy pakować state czy mape
    }
}
