package pl.edu.agh.cs.kraksim.trafficState.db

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.agh.cs.kraksim.trafficState.domain.SimulationStateEntity

interface TrafficStateRepository : JpaRepository<SimulationStateEntity, Long>
