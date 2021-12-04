package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.common.random.RandomProvider
import pl.edu.agh.cs.kraksim.core.state.Car
import kotlin.random.Random

class MockRandomProvider(
    private val booleanToReturn: Boolean = true,
    private val randomElementIndex: Int? = null,
    private var probabilityMap: HashMap<Car, Double> = HashMap()
) :
    RandomProvider {

    override fun drawWhetherShouldSlowDown() = booleanToReturn
    override fun drawWhetherShouldSlowDown(car: Car) = booleanToReturn

    override fun drawProbabilityForCar(car: Car) = probabilityMap[car]

    override fun <T> getRandomElement(list: List<T>): T {
        return randomElementIndex?.let { list[it] } ?: list[Random.nextInt(list.size)]
    }

    override fun setProbabilityForCar(car: Car, probability: Double) {
        probabilityMap[car] = probability
    }
}
