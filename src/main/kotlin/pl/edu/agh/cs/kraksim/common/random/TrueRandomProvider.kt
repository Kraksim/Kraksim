package pl.edu.agh.cs.kraksim.common.random

class TrueRandomProvider(
    private val slowDownProbability: Double
) : RandomProvider {

    init {
        require(slowDownProbability in 0.0..1.0) { "slowDownProbability should be between 0-1, but is $slowDownProbability" }
    }

    override fun drawWhetherShouldSlowDown(): Boolean {
        return Math.random() < slowDownProbability
    }
}
