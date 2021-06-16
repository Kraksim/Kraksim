package pl.edu.agh.cs.kraksim.nagelCore.nagelMovementSimulationStrategy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.MockRandomProvider
import pl.edu.agh.cs.kraksim.nagelCore.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelCar

internal class RandomizeStepNagelMovementSimulationStrategyTest {

    @Test
    fun `Given car in lane and random returning true, when randomize decrease velocity`() {
        // given
        val initialVelocity = 4
        val expectedVelocity = 3
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
        )
        car.moveToLane(state.getLine(), 0)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider(booleanToReturn = true))

        // when
        strategy.randomization(state)

        // then
        assertThat(car.velocity).isEqualTo(expectedVelocity)
    }

    @Test
    fun `Given car in lane and random returning false, when randomize don't change velocity`() {
        // given
        val initialVelocity = 4
        val expectedVelocity = 4
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
        )
        car.moveToLane(state.getLine(), 0)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider(booleanToReturn = false))

        // when
        strategy.randomization(state)

        // then
        assertThat(car.velocity).isEqualTo(expectedVelocity)
    }

    @Test
    fun `Given car with zero velocity in lane and random returning true, when randomize don't change velocity`() {
        // given
        val initialVelocity = 0
        val expectedVelocity = 0
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
        )
        car.moveToLane(state.getLine(), 0)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider(booleanToReturn = true))

        // when
        strategy.randomization(state)

        // then
        assertThat(car.velocity).isEqualTo(expectedVelocity)
    }
}
