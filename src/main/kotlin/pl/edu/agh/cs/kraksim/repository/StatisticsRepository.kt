package pl.edu.agh.cs.kraksim.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.cs.kraksim.repository.entities.statistics.StatisticsEntity

@Repository
interface StatisticsRepository : JpaRepository<StatisticsEntity, Long> {
    fun findAllBySimulationEntityId(simulationId: Long): List<StatisticsEntity>
    fun findAllBySimulationEntityIdAndTurnGreaterThanEqualAndTurnLessThanEqual(simulationId: Long, from: Long, to: Long): List<StatisticsEntity>
}
