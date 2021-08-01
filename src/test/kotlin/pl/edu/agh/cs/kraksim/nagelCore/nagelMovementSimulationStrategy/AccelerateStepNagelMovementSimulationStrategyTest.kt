package pl.edu.agh.cs.kraksim.nagelCore.nagelMovementSimulationStrategy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.agh.cs.kraksim.common.getLane
import pl.edu.agh.cs.kraksim.common.getOneRoadSimulationState
import pl.edu.agh.cs.kraksim.common.mockGps
import pl.edu.agh.cs.kraksim.common.testNagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.nagelCore.state.NagelCar

internal class AccelerateStepNagelMovementSimulationStrategyTest {

    @Test
    fun `Given car in lane, when accelerate increase velocity`() {
        // given
        val initialVelocity = 4
        val expectedVelocity = 5
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
            gps = mockGps()
        )
        car.moveToLane(state.getLane(), 0)
        val strategy = testNagelMovementSimulationStrategy()

        // when
        strategy.acceleration(state)

        // then
        assertThat(car.velocity).isEqualTo(expectedVelocity)
    }

    @Test
    fun `Given car in lane with max velocity, when accelerate don't change velocity`() {
        // given
        val initialVelocity = 6
        val state = getOneRoadSimulationState()
        val car = NagelCar(
            velocity = initialVelocity,
            gps = mockGps()
        )
        car.moveToLane(state.getLane(), 0)
        val strategy = testNagelMovementSimulationStrategy()

        // when
        strategy.acceleration(state)

        // then
        assertThat(car.velocity).isEqualTo(initialVelocity)
    }
}
