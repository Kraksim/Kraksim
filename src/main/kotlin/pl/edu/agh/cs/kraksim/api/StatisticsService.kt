package pl.edu.agh.cs.kraksim.api

import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.statistics.StateStatistics
import pl.edu.agh.cs.kraksim.statistics.StatisticsManager

@Service
class StatisticsService {

    fun createStatisticsManager(simulationId: Long, expectedVelocity: Map<RoadId, Velocity>): StatisticsManager {
        return StatisticsManager(
            getStates(simulationId),
            expectedVelocity
        )
    }

    fun getStates(simulationId: Long) = ArrayList<StateStatistics>() // TODO implement me
}
