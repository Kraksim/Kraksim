package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.brakeLight

import pl.edu.agh.cs.kraksim.common.adjacentPairs
import pl.edu.agh.cs.kraksim.common.random.RandomProvider
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelCar
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelLane
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.core.state.Car
import pl.edu.agh.cs.kraksim.core.state.Gateway
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import kotlin.math.min

class BrakeLightMovementSimulationStrategy(
    random: RandomProvider,
    maxVelocity: Int = 6,
    private val threshold: Int,
    val breakLightReactionProbability: Double,
    val accelerationDelayProbability: Double,
    val defaultProbability: Double
) : NagelMovementSimulationStrategy(random, maxVelocity) {
    override fun step(state: SimulationState) {
        brakeLightPhase(state as NagelSimulationState)
        super.step(state)
    }

    fun brakeLightPhase(state: NagelSimulationState) {
        state.lanes.filter { it.containsCar() }
            .forEach { lane -> setBLParameter(lane) }
    }

    override fun slowCar(car: NagelCar, freeSpaceInCarPath: Int) {
        if (car.velocity > freeSpaceInCarPath) {
            car.velocity = freeSpaceInCarPath
            car.brakeLightOn = true
        }
    }

    override fun randomization(state: NagelSimulationState) {
        state.cars
            .forEach { car ->
                val shouldSlowDown = car.velocity > 0 && random.drawWhetherShouldSlowDown(car)
                if (shouldSlowDown) {
                    car.velocity -= 1
                    car.brakeLightOn = true
                } else {
                    car.brakeLightOn = false
                }
            }
    }

    private fun setBLParameter(lane: NagelLane) {
        setBLAllCarsButLast(lane.cars)
        setBLLastCar(lane.cars.last())
    }

    private fun setBLAllCarsButLast(cars: MutableList<NagelCar>) {
        cars.adjacentPairs().forEach { (car, carInFront) ->
            val ts = min(car.velocity, threshold)
            val timeToReachNext =
                if (car.velocity != 0)
                    car.distanceFrom(carInFront) / car.velocity
                else
                    ts + 1
            val probability =
                if (carInFront.brakeLightOn!! && timeToReachNext < ts)
                    breakLightReactionProbability
                else if (car.velocity == 0)
                    accelerationDelayProbability
                else
                    defaultProbability
            random.setProbabilityForCar(car, probability)
        }
    }

    private fun setBLLastCar(car: NagelCar) {
        if (car.currentLane?.parentRoad?.end is Gateway) {
            if (car.velocity == 0)
                random.setProbabilityForCar(car, accelerationDelayProbability)
            else
                random.setProbabilityForCar(car, defaultProbability)
        } else {
            val distanceToMoveOnCurrentLane = min(car.distanceFromEndOfLane, car.velocity)
            val distanceTotal =
                if (car.velocity > distanceToMoveOnCurrentLane)
                    car.gps.getTargetLaneInNextRoad(this::getLane)
                        .getFreeSpaceInFront() + distanceToMoveOnCurrentLane
                else
                    distanceToMoveOnCurrentLane
            val ts = min(car.velocity, threshold)
            val timeToReachNext = if (car.velocity != 0) distanceTotal / car.velocity else ts + 1
            val carOnNextRoad: NagelCar? = car.gps.getTargetLaneInNextRoad(this::getLane).cars.getOrNull(0) as NagelCar?
            val probability =
                if (carOnNextRoad?.brakeLightOn == true && timeToReachNext < ts)
                    breakLightReactionProbability
                else if (car.velocity == 0)
                    accelerationDelayProbability else defaultProbability
            random.setProbabilityForCar(car, probability)
        }
    }
}
