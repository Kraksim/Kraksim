package pl.edu.agh.cs.kraksim.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LongArrayToStringConverterTest {

    @Test
    fun `Given not empty long list, when convert to database column, yield correct string`() {
        // given
        val converter = LongArrayToStringConverter()
        val list = arrayListOf(1L, 2L, 3L, 44L)

        // when
        val result = converter.convertToDatabaseColumn(list)

        // then
        assertThat(result).isEqualTo("1,2,3,44")
    }

    @Test
    fun `Given string, when convert to entity attribute, yield correct list`() {
        // given
        val converter = LongArrayToStringConverter()
        val string = "1,2,6,33"

        // when
        val result = converter.convertToEntityAttribute(string)

        // then
        assertThat(result).containsExactlyInAnyOrder(1L, 2L, 6L, 33L)
    }
}
