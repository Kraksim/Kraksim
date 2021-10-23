package pl.edu.agh.cs.kraksim.simulation.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationEntity
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationType

@Repository
interface SimulationRepository : JpaRepository<SimulationEntity, Long> {

    fun findAllBy(): List<BasicSimulationInfoDTO>
}

interface BasicSimulationInfoDTO {
    val id: Long
    val name: String
    val simulationType: SimulationType
    val mapEntity: MapEntityId
}

interface MapEntityId {
    val id: Long
}
