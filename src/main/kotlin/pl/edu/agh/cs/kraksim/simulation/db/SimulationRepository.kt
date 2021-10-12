package pl.edu.agh.cs.kraksim.simulation.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.cs.kraksim.simulation.domain.SimulationEntity

@Repository
interface SimulationRepository : JpaRepository<SimulationEntity, Long>
