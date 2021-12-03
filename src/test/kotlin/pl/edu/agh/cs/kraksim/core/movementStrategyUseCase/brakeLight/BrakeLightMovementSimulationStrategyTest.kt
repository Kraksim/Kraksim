package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.brakeLight

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.*
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelCar

internal class BrakeLightMovementSimulationStrategyTest {

    private val breakLightReactionProbability = 0.8
    private val accelerationDelayProbability = 0.7
    private val defaultProbability = 0.5

    @Test
    fun `test if reacts to brake lights in front`() {
        // given
        val initialVelocity = 4
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
            gps = mockGps()
        )
        val carInFront = NagelCar(
            velocity = initialVelocity,
            gps = mockGps(),
            brakeLightOn = true
        )
        car.moveToLaneFront(state.getFirstLane(), 0)
        carInFront.moveToLane(state.getFirstLane(), 8)
        val strategy = testBrakeLightMovementSimulationStrategy(
            threshold = 3,
            breakLightReactionProbability = breakLightReactionProbability,
            accelerationDelayProbability = accelerationDelayProbability,
            defaultProbability = defaultProbability
        )

        // when
        strategy.brakeLightPhase(state)

        println(strategy.random.drawProbabilityForCar(carInFront))
        // then
        Assertions.assertThat(strategy.random.drawProbabilityForCar(car)).isEqualTo(breakLightReactionProbability)
    }

    @Test
    fun `test if acceleration delay probability is applied if stopped`() {
        // given
        val initialVelocity = 0
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
            gps = mockGps()
        )
        car.moveToLaneFront(state.getFirstLane(), 0)
        val strategy = testBrakeLightMovementSimulationStrategy(
            threshold = 3,
            breakLightReactionProbability = breakLightReactionProbability,
            accelerationDelayProbability = accelerationDelayProbability,
            defaultProbability = defaultProbability
        )

        // when
        strategy.brakeLightPhase(state)

        // then
        Assertions.assertThat(strategy.random.drawProbabilityForCar(car)).isEqualTo(accelerationDelayProbability)
    }

    @Test
    fun `test if default probability applied if car in front too far`() {
        // given
        val initialVelocity = 3
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
            gps = mockGps()
        )
        val carInFront = NagelCar(
            velocity = initialVelocity,
            gps = mockGps(),
            brakeLightOn = true
        )
        car.moveToLaneFront(state.getFirstLane(), 0)
        carInFront.moveToLane(state.getFirstLane(), 15)
        val strategy = testBrakeLightMovementSimulationStrategy(
            threshold = 3,
            breakLightReactionProbability = breakLightReactionProbability,
            accelerationDelayProbability = accelerationDelayProbability,
            defaultProbability = defaultProbability
        )

        // when
        strategy.brakeLightPhase(state)

        println(strategy.random.drawProbabilityForCar(carInFront))
        // then
        Assertions.assertThat(strategy.random.drawProbabilityForCar(car)).isEqualTo(defaultProbability)
    }
}
