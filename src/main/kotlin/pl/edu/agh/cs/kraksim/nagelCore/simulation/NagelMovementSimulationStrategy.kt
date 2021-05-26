package pl.edu.agh.cs.kraksim.nagelCore.simulation

import pl.edu.agh.cs.kraksim.core.SimulationState

class NagelMovementSimulationStrategy {

    fun step(state: SimulationState) {

    }

    private fun acceleration(state: SimulationState) {
        state.roads.flatMap { it.lanes }
            .flatMap { it.cars }
            .forEach {
                if (it.velocity < MAX_VELOCITY)
                    it.velocity += 1
            }
    }

    private fun slowingDown(state: SimulationState) {
        state.roads.flatMap { it.lanes }
            .forEach {
                for ((index, car) in it.cars.withIndex()) {
                    if (index == it.cars.size - 1) {
                        continue
                    }

                    val distanceFromCarInFront =
                        it.cars[index + 1].positionRelativeToStart - car.positionRelativeToStart

                    if (car.velocity > distanceFromCarInFront) {
                        car.velocity = distanceFromCarInFront
                    }
                }

                val lastCar = it.cars[it.cars.size]

                if (it.parentRoad.end.canEnterNodeFrom(it)) {
                    // todo ostatnie auto przejazd przez skrzyżowanie (gdzie i kiedy?)
                }

            }
    }

    private fun randomization(state: SimulationState) {
        state.roads.flatMap { it.lanes }
            .flatMap { it.cars }
            .forEach {
                val shouldSlowDown = it.velocity > 0 && Math.random() < RANDOM_PROPABILITY
                if (shouldSlowDown) {
                    it.velocity -= 1
                }
            }
    }

    private fun motion(state: SimulationState) {

    }

    companion object {
        const val RANDOM_PROPABILITY = 0.5
        const val MAX_VELOCITY = 6
//    minimalna predkość to 1 kratka na sekunde, przy długości auta równej 4.5 m  to daje nam 16.2 km/h. Max velocity wtedy to 97.2 km/h
    }
}