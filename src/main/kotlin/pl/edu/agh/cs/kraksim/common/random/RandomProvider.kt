package pl.edu.agh.cs.kraksim.common.random

import pl.edu.agh.cs.kraksim.core.state.Car

interface RandomProvider {
    var probabilityMap: Map<Car, Double>

    fun drawWhetherShouldSlowDown(): Boolean
    fun drawWhetherShouldSlowDown(car: Car): Boolean
    fun <T> getRandomElement(list: List<T>): T
}
