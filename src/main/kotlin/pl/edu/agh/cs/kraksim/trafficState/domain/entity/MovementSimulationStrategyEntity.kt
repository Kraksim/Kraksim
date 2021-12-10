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
    var maxVelocity: Int,
    var threshold: Int? = null,
    var accelerationDelayProbability: Double? = null,
    var breakLightReactionProbability: Double? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class MovementSimulationStrategyType {
    NAGEL_SCHRECKENBERG,
    MULTI_LANE_NAGEL_SCHRECKENBERG,
    BRAKE_LIGHT;

    companion object {
        private val singleLaneBasedStrategies
            get() = listOf(NAGEL_SCHRECKENBERG, BRAKE_LIGHT)

        fun isSingleLaneBased(strategy: MovementSimulationStrategyType): Boolean {
            return singleLaneBasedStrategies.contains(strategy)
        }
    }
}

enum class RandomProviderType {
    TRUE
}
