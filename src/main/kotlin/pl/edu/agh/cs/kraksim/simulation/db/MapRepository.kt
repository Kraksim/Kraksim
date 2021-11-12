package pl.edu.agh.cs.kraksim.simulation.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pl.edu.agh.cs.kraksim.simulation.domain.BasicMapInfoDTO
import pl.edu.agh.cs.kraksim.simulation.domain.MapEntity
import pl.edu.agh.cs.kraksim.simulation.domain.MapType

@Repository
interface MapRepository : JpaRepository<MapEntity, Long> {

    @Query("SELECT new pl.edu.agh.cs.kraksim.simulation.domain.BasicMapInfoDTO(e.type, e.name, e.id) from MapEntity e")
    fun getAllMapsBasicInfo(): List<BasicMapInfoDTO>
}
