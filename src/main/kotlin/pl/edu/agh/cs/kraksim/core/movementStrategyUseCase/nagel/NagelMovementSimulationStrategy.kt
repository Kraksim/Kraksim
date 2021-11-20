package pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel

import pl.edu.agh.cs.kraksim.common.adjacentPairs
import pl.edu.agh.cs.kraksim.common.random.RandomProvider
import pl.edu.agh.cs.kraksim.common.takeEachWhile
import pl.edu.agh.cs.kraksim.common.withoutLast
import pl.edu.agh.cs.kraksim.core.MovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.movementStrategyUseCase.nagel.state.*
import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import kotlin.math.min

open class NagelMovementSimulationStrategy(
    val random: RandomProvider,
    val maxVelocity: Int = 6
) : MovementSimulationStrategy {

    init {
//    minimalna predkość to 1 kratka na sekunde, przy długości auta równej 4.5 m to daje nam 16.2 km/h. Max velocity wtedy to 97.2 km/h
        require(maxVelocity > 0) { "maxVelocity should be positive, but is $maxVelocity" }
    }

    override fun step(state: SimulationState) {
        acceleration(state as NagelSimulationState)
        slowingDown(state)
        randomization(state)
        motion(state)
        resolveIntersections(state)
        state.turn++
    }

    fun acceleration(state: NagelSimulationState) {
        state.cars
            .forEach { car ->
                if (car.velocity < maxVelocity)
                    car.velocity += 1
            }
    }

    fun slowingDown(state: NagelSimulationState) {
        state.lanes.filter { it.containsCar() }
            .forEach { lane -> slowCars(lane) }
    }

    private fun slowCars(lane: NagelLane) {
        slowAllCarsButLast(lane.cars)
        slowLastCar(
            endNode = lane.parentRoad.end,
            lane = lane,
            lastCar = lane.cars.last()
        )
    }

    private fun slowAllCarsButLast(cars: MutableList<NagelCar>) {
        cars.adjacentPairs().forEach { (car, carInFront) ->
            car.velocity = min(car.velocity, car.distanceFrom(carInFront))
        }
    }

    protected open fun slowLastCar(endNode: NagelRoadNode, lane: NagelLane, lastCar: NagelCar) {
        if (endNode is NagelIntersection) {
            val destinationLane = getTargetLane(lastCar)

            val freeSpaceInCarPath =
                if (endNode.canGoThrough(lane)) {
                    lastCar.distanceFromEndOfLane + destinationLane.getFreeSpaceInFront()
                } else {
                    lastCar.distanceFromEndOfLane
                }

            lastCar.velocity = min(lastCar.velocity, freeSpaceInCarPath)
        }
    }

    fun randomization(state: NagelSimulationState) {
        state.cars
            .forEach { car ->
                val shouldSlowDown = car.velocity > 0 && random.drawWhetherShouldSlowDown()
                if (shouldSlowDown) {
                    car.velocity -= 1
                }
            }
    }

    fun motion(state: NagelSimulationState) {
        state.lanes.filter { it.containsCar() }
            .forEach { lane -> moveCars(lane) }
    }

    private fun moveCars(lane: NagelLane) {
        moveAllCarsButLast(lane.cars)
        moveLastCar(
            lastCar = lane.cars.last(),
            endNode = lane.parentRoad.end
        )
    }

    private fun moveAllCarsButLast(cars: MutableList<NagelCar>) {
        cars.withoutLast().forEach { car ->
            car.positionRelativeToStart += car.velocity
        }
    }

    private fun moveLastCar(lastCar: NagelCar, endNode: NagelRoadNode) {
        val distanceToMoveOnCurrentLane = min(lastCar.distanceFromEndOfLane, lastCar.velocity)
        lastCar.moveForward(distanceToMoveOnCurrentLane)

        if (lastCar.hasDistanceLeftToMove() && endNode is NagelGateway) {
            endNode.addFinishedCar(lastCar)
        }
    }

    fun resolveIntersections(state: NagelSimulationState) {
        getCarsToResolve(state.roads.values).forEach { (destinationLane, cars) ->
            var spaceLeft = destinationLane.getFreeSpaceInFront()

            cars.sortedByDescending { car -> car.distanceLeftToMove }
                .takeEachWhile({ spaceLeft > 0 }) { car ->
                    val newPosition = min(spaceLeft, car.distanceLeftToMove) - 1

                    car.moveToLaneFront(
                        destinationLane,
                        newPosition
                    )
                    car.gps.popNext()
                    spaceLeft = newPosition
                }
        }
    }

    private fun getCarsToResolve(roads: Collection<NagelRoad>): Map<NagelLane, List<NagelCar>> {
        return roads.filter { it.end is NagelIntersection }
            .flatMap { it.lanes }
            .mapNotNull { it.cars.lastOrNull() }
            .filter { it.hasDistanceLeftToMove() }
            .groupByTo(hashMapOf()) {
                val targetLane = getTargetLane(it)
                it.gps.getNext()
                targetLane
            }
    }

    private fun getTargetLane(lastCar: NagelCar) =
        lastCar.gps.getTargetLaneInNextRoad(this::getLane) as NagelLane

    protected open fun getLane(road: Road) = road.lanes[0]
}
