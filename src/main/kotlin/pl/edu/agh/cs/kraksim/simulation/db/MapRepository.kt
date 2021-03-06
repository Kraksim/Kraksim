package pl.edu.agh.cs.kraksim.simulation.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.cs.kraksim.simulation.domain.MapEntity

@Repository
interface MapRepository : JpaRepository<MapEntity, Long> {
    fun findAllByOrderById(): List<MapEntity>
}
