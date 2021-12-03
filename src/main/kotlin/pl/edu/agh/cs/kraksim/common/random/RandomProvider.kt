package pl.edu.agh.cs.kraksim.common.random

import pl.edu.agh.cs.kraksim.core.state.Car

interface RandomProvider {
    fun drawWhetherShouldSlowDown(): Boolean
    fun drawWhetherShouldSlowDown(car: Car): Boolean
    fun <T> getRandomElement(list: List<T>): T
    fun setProbabilityForCar(car: Car, probability: Double)
    fun drawProbabilityForCar(car: Car): Double?
}
