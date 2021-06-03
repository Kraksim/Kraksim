package pl.edu.agh.cs.kraksim.comon

import org.springframework.stereotype.Component

@Component
class RandomProvider {

    fun get(): Double {
        return Math.random()
    }
}