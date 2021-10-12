package pl.edu.agh.cs.kraksim.statistics

import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.repository.StatisticsRepository
import pl.edu.agh.cs.kraksim.repository.entities.statistics.StatisticsEntity
import java.util.*

@Service
class StatisticsService(
    val repository: StatisticsRepository
) {
    fun getStatisticsFromSimulation(simulationId: Long): List<StatisticsEntity> {
        return repository.findAllBySimulationEntityId(simulationId)
    }

    fun getStatisticsFromSimulationBetweenTurns(simulationId: Long, from: Long, to: Long): List<StatisticsEntity> {
        return repository.findAllBySimulationEntityIdAndTurnGreaterThanEqualAndTurnLessThanEqual(simulationId, from, to)
    }

    fun findById(id: Long): Optional<StatisticsEntity> {
        return repository.findById(id)
    }
}
