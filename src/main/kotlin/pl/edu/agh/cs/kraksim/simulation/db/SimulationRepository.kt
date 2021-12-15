package pl.edu.agh.cs.kraksim.simulation.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pl.edu.agh.cs.kraksim.common.MapId
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationEntity
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationType
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType
import javax.persistence.LockModeType

@Repository
interface SimulationRepository : JpaRepository<SimulationEntity, Long> {

    fun findAllBy(): List<BasicSimulationInfo>

    fun findAllByIdIn(id: List<Long>): List<BasicSimulationInfo>

    @Query("SELECT new kotlin.Pair(e.mapEntity.id, COUNT(e.id)) from SimulationEntity e WHERE e.mapEntity.id in ?1 GROUP BY e.mapEntity.id")
    fun countAllByMapEntity(mapIds: List<Long>): List<Pair<MapId, Long>>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SimulationEntity s WHERE s.id=?1")
    fun getByIdWithLock(id: Long): SimulationEntity?
}

interface BasicSimulationInfo {
    val id: Long
    val name: String
    val simulationType: SimulationType
    val mapEntity: MapEntityId
    val simulationStateEntities: List<SimulationStateEntityTurn>
    var finished: Boolean
    var movementSimulationStrategy: MovementSimulationStrategyEntityType
}

interface MapEntityId {
    val id: Long
}

interface SimulationStateEntityTurn {
    val turn: Long
}

interface MovementSimulationStrategyEntityType {
    var type: MovementSimulationStrategyType
}
