package pl.edu.agh.cs.kraksim.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CachedValueTest {

    @Test
    fun `Given cached value, when get return value based on populator`() {
        // given
        val populator: (String) -> Int = {
            it.length
        }
        val cachedValue = CachedValue(populator)

        // when
        val result = cachedValue.get("Hello")

        // then
        assertThat(result).isEqualTo(5)
    }

    @Test
    @Suppress("UNUSED_VARIABLE")
    fun `Given cached value, when get with different value return value based on populator called on new value`() {
        // given
        val populator: (String) -> Int = {
            it.length
        }
        val cachedValue = CachedValue(populator)
        val firstCache = cachedValue.get("Hello")

        // when
        val result = cachedValue.get("Hello2")

        // then
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun `Given cached value, when get 2 times call populator only once`() {
        // given
        var callCounter = 0
        val populator: (String) -> Int = {
            callCounter++
            it.length
        }
        val cachedValue = CachedValue(populator)

        // when
        cachedValue.get("Hello")
        cachedValue.get("Hello")
        val result = cachedValue.get("Hello")

        // then
        assertThat(result).isEqualTo(5)
        assertThat(callCounter).isEqualTo(1)
    }
}
