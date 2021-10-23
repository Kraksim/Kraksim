package pl.edu.agh.cs.kraksim.statistics.application

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.statistics.db.StatisticsRepository
import pl.edu.agh.cs.kraksim.statistics.domain.StatisticsEntity

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

    fun findById(id: Long): StatisticsEntity? {
        return repository.findByIdOrNull(id)
    }
}
