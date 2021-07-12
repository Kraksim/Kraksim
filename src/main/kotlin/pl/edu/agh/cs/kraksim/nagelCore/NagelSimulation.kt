package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.core.Simulation
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.statistics.StatisticsManager
import pl.edu.agh.cs.kraksim.trafficLight.LightPhaseManager

class NagelSimulation(
    override val state: NagelSimulationState,
    override val movementSimulationStrategy: NagelMovementSimulationStrategy,
    override val lightPhaseManager: LightPhaseManager,
    override val statisticsManager: StatisticsManager
) : Simulation {

    override fun step() {
        movementSimulationStrategy.step(state)
        lightPhaseManager.changeLights()
        statisticsManager.createStatistics(state)
    }
}
