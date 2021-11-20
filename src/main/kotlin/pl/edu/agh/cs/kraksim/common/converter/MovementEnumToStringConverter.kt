package pl.edu.agh.cs.kraksim.common.converter

import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType
import javax.persistence.AttributeConverter

class MovementEnumToStringConverter : AttributeConverter<List<MovementSimulationStrategyType>, String> {
    override fun convertToDatabaseColumn(attribute: List<MovementSimulationStrategyType>?): String? {
        return attribute?.joinToString(",")
    }

    override fun convertToEntityAttribute(dbData: String?): List<MovementSimulationStrategyType> =
        when (dbData) {
            null, "" -> emptyList()
            else -> dbData.split(",").map { MovementSimulationStrategyType.valueOf(it) }
        }
}
