package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.multilaneNagel

import pl.edu.agh.cs.kraksim.common.Direction
import pl.edu.agh.cs.kraksim.common.random.RandomProvider
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.NagelMovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelCar
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelLane
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelRoadNode
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.NagelSimulationState
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import java.lang.Integer.max
import kotlin.math.min

class MultiLaneNagelMovementSimulationStrategy(
    random: RandomProvider,
    maxVelocity: Int = 6,
    private val distanceFromIntersectionWhenBiasCarToChangeLane: Int = maxVelocity * 5
) : NagelMovementSimulationStrategy(random, maxVelocity) {

    override fun step(state: SimulationState) {
        changeLanes(state as NagelSimulationState)
        super.step(state)
    }

    fun changeLanes(state: NagelSimulationState) {
        val direction = Direction.basedOnTurn(state.turn)
        val cars = state.cars
        var toChange = getCarsThatCouldOvertake(cars, direction)
        toChange = getCarsThatCouldChangeToDestinationLane(cars, direction, toChange)

        toChange.forEach { car ->
            val currentLane = car.currentLane!!
            val destinationLane = currentLane.getNeighbouringLane(direction)!!
            car.moveToLane(destinationLane, car.positionRelativeToStart)
        }
    }

    private fun getCarsThatCouldOvertake(cars: List<NagelCar>, direction: Direction): Set<NagelCar> {
        return cars.filter { car -> canCarOvertake(car, direction) }
            .toSet()
    }

    private fun getCarsThatCouldChangeToDestinationLane(
        cars: List<NagelCar>,
        direction: Direction,
        alreadyMarked: Set<NagelCar>
    ): Set<NagelCar> {
        return cars.filter { !alreadyMarked.contains(it) }
            .filter { car -> shouldCarChangeLaneToDestination(car, direction) }
            .toSet() + alreadyMarked
    }

    private fun canCarOvertake(
        car: NagelCar,
        direction: Direction
    ): Boolean {
        val currentLane = car.currentLane!!
        val candidateLane: NagelLane = currentLane.getNeighbouringLane(direction) ?: return false

        val position = car.positionRelativeToStart
        if (!candidateLane.isSpaceOccupied(position)) {
            val (weight1, weight2, weight3) = get3Weights(candidateLane, position, currentLane, car)

            val weight4 = if (currentLane.doesLaneEndBeforeEndNode()) { // the closer the car is to end of lane the more likely it is to overtake
                max(maxVelocity - car.distanceFromEndOfLane, 0)
            } else 0

            return weight1 + weight4 > weight2 && weight1 + weight4 > weight3
        }
        return false
    }

    private fun shouldCarChangeLaneToDestination(
        car: NagelCar,
        direction: Direction
    ): Boolean {
        val currentLane = car.currentLane!!

        val wantedChangeDirection = car.getChangeLaneDirection()
        if (wantedChangeDirection != direction) return false

        val candidateLane: NagelLane = currentLane.getNeighbouringLane(direction) ?: return false

        val position = car.positionRelativeToStart
        if (!candidateLane.isSpaceOccupied(position)) {
            val (weight1, weight2, weight3) = get3Weights(candidateLane, position, currentLane, car)
            val weight4 = max(
                (distanceFromIntersectionWhenBiasCarToChangeLane - car.distanceFromEndOfLane) / maxVelocity,
                0
            )

            return weight1 + weight4 > weight2 && weight1 + weight4 > weight3
        }
        return false
    }

    private fun get3Weights(
        candidateLane: NagelLane,
        position: Int,
        currentLane: NagelLane,
        car: NagelCar
    ): Triple<Int, Int, Int> {
        val (carBefore, carAfter) = candidateLane.getCarsBeforeAndAfterPosition(position)

        val gapInFrontOfCurrentCar = currentLane.getFreeSpaceInFrontOf(car)
        val gapInFrontOnNextLane = carAfter?.distanceFrom(car) ?: candidateLane.getSpaceToEndFrom(position)
        val gapBehindOnNextLane = carBefore?.distanceFrom(car) ?: maxVelocity

        val weight1 =
            if (gapInFrontOfCurrentCar < car.velocity && gapInFrontOnNextLane > gapInFrontOfCurrentCar) 1 else 0
        val weight2 = car.velocity - gapInFrontOnNextLane
        val weight3 = maxVelocity - gapBehindOnNextLane
        return Triple(weight1, weight2, weight3)
    }

    override fun slowLastCar(endNode: NagelRoadNode, lane: NagelLane, lastCar: NagelCar) {
        if (lane.doesLaneEndBeforeEndNode()) {
            lastCar.velocity = min(lastCar.velocity, lastCar.distanceFromEndOfLane)
        } else {
            super.slowLastCar(endNode, lane, lastCar)
        }
    }
}
