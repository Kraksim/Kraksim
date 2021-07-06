package pl.edu.agh.cs.kraksim.repository

import javax.persistence.AttributeConverter

class LongArrayToStringConverter : AttributeConverter<List<Long>, String> {
    override fun convertToDatabaseColumn(attribute: List<Long>?): String? {
        return attribute?.joinToString(",")
    }

    override fun convertToEntityAttribute(dbData: String?): List<Long> {
        return dbData?.split(",")?.map { it.toLong() } ?: emptyList()
    }
}