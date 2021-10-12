package pl.edu.agh.cs.kraksim.trafficState.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.CarEntity

@Repository
interface CarRepository : JpaRepository<CarEntity, Long> {
    fun findCarEntitiesByCarId(carId: Long): List<CarEntity>
}
