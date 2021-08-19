package pl.edu.agh.cs.kraksim.api

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.cs.kraksim.repository.entities.RoadNodeEntity

@Repository // temporary for test
interface RoadNodeRepository : JpaRepository<RoadNodeEntity, Long> {

    fun findByStartingRoadsIsNotNull(): RoadNodeEntity
    fun findByStartingRoadsIsNull(): RoadNodeEntity
}
