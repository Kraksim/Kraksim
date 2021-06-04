package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.comon.random.RandomProvider

class MockRandomProvider(private val booleanToReturn: Boolean = true) : RandomProvider {

    override fun getBoolean(trueProbability: Double) = booleanToReturn
}