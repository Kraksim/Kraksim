package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.common.random.RandomProvider
import pl.edu.agh.cs.kraksim.model.movementSimulation.nagel.NagelMovementSimulationStrategy

fun testNagelMovementSimulationStrategy(randomProvider: RandomProvider = MockRandomProvider()) =
    NagelMovementSimulationStrategy(randomProvider)
