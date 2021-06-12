package pl.edu.agh.cs.kraksim.nagelCore.simulation.nagelMovementSimulationStrategy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.MockRandomProvider

import pl.edu.agh.cs.kraksim.nagelCore.NagelCar
import pl.edu.agh.cs.kraksim.nagelCore.simulation.NagelMovementSimulationStrategy

internal class AccelerateStepNagelMovementSimulationStrategyTest {

    @Test
    fun `Given car in lane, when accelerate increase velocity`() {
        // given
        val initialVelocity = 4
        val expectedVelocity = 5
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
        )
        car.moveToLane(state.getLine(), 0)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.acceleration(state)

        //then
        assertThat(car.velocity).isEqualTo(expectedVelocity)
    }

    @Test
    fun `Given car in lane with max velocity, when accelerate don't change velocity`() {
        // given
        val initialVelocity = 6
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
        )
        car.moveToLane(state.getLine(), 0)
        val strategy = NagelMovementSimulationStrategy(MockRandomProvider())

        // when
        strategy.acceleration(state)

        //then
        assertThat(car.velocity).isEqualTo(initialVelocity)
    }
}
