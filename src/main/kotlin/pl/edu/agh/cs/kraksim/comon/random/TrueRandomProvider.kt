package pl.edu.agh.cs.kraksim.comon.random

import org.springframework.stereotype.Component

@Component
class TrueRandomProvider : RandomProvider {

    override fun getBool(trueProbability: Double): Boolean {
        return Math.random() < trueProbability
    }
}