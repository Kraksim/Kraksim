package pl.edu.agh.cs.kraksim.common.random

import kotlin.random.Random

class TrueRandomProvider(
    private val slowDownProbability: Double
) : RandomProvider {

    init {
        require(slowDownProbability in 0.0..1.0) { "slowDownProbability should be between 0-1, but is $slowDownProbability" }
    }

    override fun drawWhetherShouldSlowDown(): Boolean {
        return Math.random() < slowDownProbability
    }

    override fun <T> getRandomElement(list: List<T>): T {
        require(list.isNotEmpty()) { "List must have at least one element to choose" }
        val randomIndex = Random.nextInt(list.size)
        return list[randomIndex]
    }
}
