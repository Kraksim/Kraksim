package pl.edu.agh.cs.kraksim.trafficState.domain.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class MovementSimulationStrategyEntity(
    var type: MovementSimulationStrategyType,
    var randomProvider: RandomProviderType,
    var slowDownProbability: Double,
    var maxVelocity: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class MovementSimulationStrategyType {
    NAGEL_SCHRECKENBERG
}

enum class RandomProviderType {
    TRUE
}
