package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.common.random.RandomProvider
import kotlin.random.Random

class MockRandomProvider(private val booleanToReturn: Boolean = true, private val randomElementIndex: Int? = null) :
    RandomProvider {

    override fun drawWhetherShouldSlowDown() = booleanToReturn
    override fun <T> getRandomElement(list: List<T>): T {
        return randomElementIndex?.let { list[it] } ?: list[Random.nextInt(list.size)]
    }
}
