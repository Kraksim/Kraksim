package pl.edu.agh.cs.kraksim.common.random

import org.springframework.stereotype.Component

@Component
class TrueRandomProvider : RandomProvider {

    override fun getBoolean(trueProbability: Double): Boolean {
        return Math.random() < trueProbability
    }
}
