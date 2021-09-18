package pl.edu.agh.cs.kraksim.statistics

import org.springframework.stereotype.Service
import pl.edu.agh.cs.kraksim.repository.StatisticsRepository

@Service
class StatisticsService(
    repository: StatisticsRepository
)
