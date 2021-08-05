package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.common.random.RandomProvider

class MockRandomProvider(private val booleanToReturn: Boolean = true) : RandomProvider {

    override fun drawWhetherShouldSlowDown() = booleanToReturn
}
