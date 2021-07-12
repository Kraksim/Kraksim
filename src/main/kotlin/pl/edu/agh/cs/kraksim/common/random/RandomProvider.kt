package pl.edu.agh.cs.kraksim.common.random

interface RandomProvider {
    fun drawWhetherShouldSlowDown(): Boolean
}
