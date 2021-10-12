package pl.edu.agh.cs.kraksim.common.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class MapToStringConverter : AttributeConverter<Map<Long, Double>, String> {
    override fun convertToDatabaseColumn(attribute: Map<Long, Double>?): String? {
        return attribute?.map {
            "${it.key}=${it.value}"
        }?.joinToString(separator = ",")
    }

    override fun convertToEntityAttribute(dbData: String?): Map<Long, Double> {
        return dbData?.split(",")
            ?.filter { it.isNotBlank() }
            ?.associate {
                val (keyString, valueString) = it.split("=")
                keyString.toLong() to valueString.toDouble()
            } ?: emptyMap()
    }
}
