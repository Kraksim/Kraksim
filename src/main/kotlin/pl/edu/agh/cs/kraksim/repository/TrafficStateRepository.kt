package pl.edu.agh.cs.kraksim.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.SimulationStateEntity

interface TrafficStateRepository : JpaRepository<SimulationStateEntity, Long>
