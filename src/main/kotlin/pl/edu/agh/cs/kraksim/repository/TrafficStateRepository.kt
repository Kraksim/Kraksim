package pl.edu.agh.cs.kraksim.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.agh.cs.kraksim.repository.entities.TrafficStateEntity

interface TrafficStateRepository : JpaRepository<TrafficStateEntity, Long>