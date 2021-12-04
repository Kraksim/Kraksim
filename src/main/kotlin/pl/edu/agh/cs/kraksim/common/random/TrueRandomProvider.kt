package pl.edu.agh.cs.kraksim.common.random

import pl.edu.agh.cs.kraksim.core.state.Car
import kotlin.random.Random

class TrueRandomProvider(
    val slowDownProbability: Double
) : RandomProvider {

    init {
        require(slowDownProbability in 0.0..1.0) { "slowDownProbability should be between 0-1, but is $slowDownProbability" }
    }

    private val probabilityMap: HashMap<Car, Double> = HashMap()

    override fun drawWhetherShouldSlowDown(): Boolean {
        return Math.random() < slowDownProbability
    }

    override fun drawWhetherShouldSlowDown(car: Car): Boolean {
        return Math.random() < probabilityMap[car] ?: throw IllegalStateException("Probability Map doesn't include mapping for this car")
    }

    override fun <T> getRandomElement(list: List<T>): T {
        require(list.isNotEmpty()) { "List must have at least one element to choose" }
        val randomIndex = Random.nextInt(list.size)
        return list[randomIndex]
    }

    override fun setProbabilityForCar(car: Car, probability: Double) {
        probabilityMap[car] = probability
    }

    override fun drawProbabilityForCar(car: Car): Double? = probabilityMap[car]
}
