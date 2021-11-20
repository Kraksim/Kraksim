package pl.edu.agh.cs.kraksim.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.converter.LongArrayToStringConverter
import pl.edu.agh.cs.kraksim.common.converter.MovementEnumToStringConverter
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType.*

internal class MovementEnumArrayToStringConverterTest {

    @Test
    fun `Given not empty long list, when convert to database column, yield correct string`() {
        // given
        val converter = MovementEnumToStringConverter()
        val list = arrayListOf(NAGEL_SCHRECKENBERG, MULTI_LANE_NAGEL_SCHRECKENBERG)

        // when
        val result = converter.convertToDatabaseColumn(list)

        // then
        assertThat(result).isEqualTo("NAGEL_SCHRECKENBERG,MULTI_LANE_NAGEL_SCHRECKENBERG")
    }

    @Test
    fun `Given string, when convert to entity attribute, yield correct list`() {
        // given
        val converter = MovementEnumToStringConverter()
        val string = "NAGEL_SCHRECKENBERG,MULTI_LANE_NAGEL_SCHRECKENBERG"

        // when
        val result = converter.convertToEntityAttribute(string)

        // then
        assertThat(result).containsExactlyInAnyOrder(NAGEL_SCHRECKENBERG, MULTI_LANE_NAGEL_SCHRECKENBERG)
    }
}
