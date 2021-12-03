package pl.edu.agh.cs.kraksim.common

import pl.edu.agh.cs.kraksim.common.random.RandomProvider
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.brakeLight.BrakeLightMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.multilaneNagel.MultiLaneNagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.NagelMovementSimulationStrategy

fun testNagelMovementSimulationStrategy(randomProvider: RandomProvider = MockRandomProvider()) =
    NagelMovementSimulationStrategy(randomProvider)

fun testMultiLaneNagelMovementSimulationStrategy(randomProvider: RandomProvider = MockRandomProvider()) =
    MultiLaneNagelMovementSimulationStrategy(randomProvider)

fun testBrakeLightMovementSimulationStrategy(
    randomProvider: RandomProvider = MockRandomProvider(),
    threshold: Int,
    breakLightReactionProbability: Double,
    accelerationDelayProbability: Double,
    defaultProbability: Double
) =
    BrakeLightMovementSimulationStrategy(
        randomProvider,
        threshold = threshold,
        breakLightReactionProbability = breakLightReactionProbability,
        accelerationDelayProbability = accelerationDelayProbability,
        defaultProbability = defaultProbability
    )
