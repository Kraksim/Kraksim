package pl.edu.agh.cs.kraksim.simulation.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pl.edu.agh.cs.kraksim.simulation.domain.MapEntity

@Repository
interface MapRepository : JpaRepository<MapEntity, Long> {

    @Query("SELECT new pl.edu.agh.cs.kraksim.simulation.db.MapId(e.id) from MapEntity e")
    fun findAllIds(): List<MapId>
}

class MapId(
    var id: Long
)
