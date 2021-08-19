package pl.edu.agh.cs.kraksim.api

import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.common.Velocity
import pl.edu.agh.cs.kraksim.repository.entities.SimulationEntity
import pl.edu.agh.cs.kraksim.repository.entities.statistics.SpeedStatisticsEntity
import pl.edu.agh.cs.kraksim.repository.entities.statistics.StatisticsEntity
import pl.edu.agh.cs.kraksim.repository.entities.statistics.StatisticsValuesEntity
import pl.edu.agh.cs.kraksim.statistics.SpeedStatistics
import pl.edu.agh.cs.kraksim.statistics.StateStatistics
import pl.edu.agh.cs.kraksim.statistics.StatisticsManager
import pl.edu.agh.cs.kraksim.statistics.StatisticsValues

@Service
class StatisticsService {

    fun createStatisticsManager(
        statisticsEntities: List<StatisticsEntity>,
        expectedVelocity: Map<RoadId, Velocity>
    ): StatisticsManager {
        return StatisticsManager(
            createStateStatistics(statisticsEntities),
            expectedVelocity
        )
    }

    private fun createStateStatistics(statisticsEntities: List<StatisticsEntity>): List<StateStatistics> {
        return statisticsEntities.map {
            StateStatistics(
                simulationId = it.simulationEntity.id,
                turn = it.turn,
                currentStatisticsValues = StatisticsValues(
                    speedStatistics = SpeedStatistics(
                        wholeMapAverageSpeed = it.currentStatisticsValues.speedStatistics.wholeMapAverageSpeed,
                        roadAverageSpeed = it.currentStatisticsValues.speedStatistics.roadAverageSpeed
                    ),
                    density = it.currentStatisticsValues.density,
                    roadFlowRatio = it.currentStatisticsValues.roadFlowRatio
                ),
                totalStatisticsValues = StatisticsValues(
                    speedStatistics = SpeedStatistics(
                        wholeMapAverageSpeed = it.totalStatisticsValues.speedStatistics.wholeMapAverageSpeed,
                        roadAverageSpeed = it.totalStatisticsValues.speedStatistics.roadAverageSpeed
                    ),
                    density = it.totalStatisticsValues.density,
                    roadFlowRatio = it.totalStatisticsValues.roadFlowRatio
                ),
            )
        }
    }

    fun createStatisticsEntity(statisticsEntity: StateStatistics, simulationEntity: SimulationEntity): StatisticsEntity {
        return StatisticsEntity(
                simulationEntity = simulationEntity,
                turn = statisticsEntity.turn,
                currentStatisticsValues = StatisticsValuesEntity(
                    speedStatistics = SpeedStatisticsEntity(
                        wholeMapAverageSpeed = statisticsEntity.currentStatisticsValues.speedStatistics.wholeMapAverageSpeed,
                        roadAverageSpeed = statisticsEntity.currentStatisticsValues.speedStatistics.roadAverageSpeed
                    ),
                    density = statisticsEntity.currentStatisticsValues.density,
                    roadFlowRatio = statisticsEntity.currentStatisticsValues.roadFlowRatio
                ),
                totalStatisticsValues = StatisticsValuesEntity(
                    speedStatistics = SpeedStatisticsEntity(
                        wholeMapAverageSpeed = statisticsEntity.totalStatisticsValues.speedStatistics.wholeMapAverageSpeed,
                        roadAverageSpeed = statisticsEntity.totalStatisticsValues.speedStatistics.roadAverageSpeed
                    ),
                    density = statisticsEntity.totalStatisticsValues.density,
                    roadFlowRatio = statisticsEntity.totalStatisticsValues.roadFlowRatio
                ),
            )
        
    }
}
