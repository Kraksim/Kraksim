package pl.edu.agh.cs.kraksim.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.CarEntity

@Repository
interface CarRepository : JpaRepository<CarEntity, Long>
