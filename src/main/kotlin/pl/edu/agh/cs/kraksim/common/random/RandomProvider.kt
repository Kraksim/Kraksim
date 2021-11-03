package pl.edu.agh.cs.kraksim.common.random

interface RandomProvider {
    fun drawWhetherShouldSlowDown(): Boolean
    fun <T> getRandomElement(list: List<T>): T
}
