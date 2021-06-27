package pl.edu.agh.cs.kraksim.nagelCore

import pl.edu.agh.cs.kraksim.common.adjacentPairs
import pl.edu.agh.cs.kraksim.common.random.RandomProvider
import pl.edu.agh.cs.kraksim.common.withoutLast
import pl.edu.agh.cs.kraksim.core.MovementSimulationStrategy
import pl.edu.agh.cs.kraksim.core.state.SimulationState
import pl.edu.agh.cs.kraksim.nagelCore.state.*
import kotlin.math.min

class NagelMovementSimulationStrategy(
    private val random: RandomProvider
) : MovementSimulationStrategy {

    override fun step(state: SimulationState) {
        acceleration(state as NagelSimulationState)
        slowingDown(state)
        randomization(state)
        motion(state)
        resolveIntersections(state)
    }

    fun acceleration(state: NagelSimulationState) {
        state.cars
            .forEach { car ->
                if (car.velocity < MAX_VELOCITY)
                    car.velocity += 1
            }
    }

    fun slowingDown(state: NagelSimulationState) {
        state.lanes.filter { it.containsCar() }
            .forEach { lane -> slowCars(lane) }
    }

    private fun slowCars(lane: NagelLane) {
        slowAllCarsButLast(lane.cars)
        // todo pamietać o pasach ze się mogą skończyć
        slowLastCar(
            endNode = lane.parentRoad.end(),
            lane = lane,
            lastCar = lane.cars.last()
        )
    }

    private fun slowAllCarsButLast(cars: MutableList<NagelCar>) {
        cars.adjacentPairs().forEach { (car, carInFront) ->
            car.velocity = min(car.velocity, car.distanceFrom(carInFront))
        }
    }

    private fun slowLastCar(endNode: NagelRoadNode, lane: NagelLane, lastCar: NagelCar) {
        if (endNode is NagelIntersection) {
            // TODO zastanowić się co z pasem
            val destinationLane = lastCar.gps.getNext().lanes[0]

            val freeSpaceInCarPath =
                if (endNode.canGoThrough(lane)) {
                    lastCar.distanceFromRoadNode + destinationLane.getFreeSpaceInFront()
                } else {
                    lastCar.distanceFromRoadNode
                }

            lastCar.velocity = min(lastCar.velocity, freeSpaceInCarPath)
        }
    }

    fun randomization(state: NagelSimulationState) {
        state.cars
            .forEach { car ->
                val shouldSlowDown = car.velocity > 0 && random.getBoolean(SLOW_DOWN_PROBABILITY)
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

        // todo pamietać o pasach ze się mogą skończyć
        moveLastCar(
            lastCar = lane.cars.last(),
            endNode = lane.parentRoad.end()
        )
    }

    private fun moveAllCarsButLast(cars: MutableList<NagelCar>) {
        cars.withoutLast().forEach { car ->
            car.positionRelativeToStart += car.velocity
        }
    }

    private fun moveLastCar(lastCar: NagelCar, endNode: NagelRoadNode) {
        val distanceToMoveOnCurrentLane = min(lastCar.distanceFromRoadNode, lastCar.velocity)
        lastCar.moveForward(distanceToMoveOnCurrentLane)

        if (lastCar.hasDistanceLeftToMove() && endNode is NagelGateway) {
            endNode.addFinishedCar(lastCar)
        }
    }

    // TODO refactor
    fun resolveIntersections(state: NagelSimulationState) {
        getCarsToResolve(state.roads).forEach { (destinationLane, cars) ->
            var spaceLeft = destinationLane.getFreeSpaceInFront()

            cars.sortedByDescending { car -> car.distanceLeftToMove }
                .takeWhile { spaceLeft > 0 }
                .forEach { car ->
                    val newPosition = min(spaceLeft, car.distanceLeftToMove) - 1

                    car.moveToLane(
                        destinationLane,
                        newPosition
                    ) // todo zamiast move to zapisać i ruszyć wszystkie naraz
                    spaceLeft = newPosition
                }
        }
    }

    // TODO refactor
    private fun getCarsToResolve(roads: List<NagelRoad>): HashMap<NagelLane, ArrayList<NagelCar>> {
        val carsToResolve = HashMap<NagelLane, ArrayList<NagelCar>>()
        roads.filter { it.end() is NagelIntersection }
            .flatMap { it.lanes }
            .filter { it.containsCar() }
            .filter { it.cars.last().hasDistanceLeftToMove() }
            .forEach {
                val lastCar = it.cars.last()

                // todo zastanowić się co z pasem
                val lane = lastCar.gps.popNext().lanes[0] as NagelLane

                val cars: ArrayList<NagelCar> = carsToResolve[lane] ?: ArrayList()
                cars.add(lastCar)
                carsToResolve[lane] = cars
            }
        return carsToResolve
    }

    companion object {
        const val SLOW_DOWN_PROBABILITY = 0.5
        const val MAX_VELOCITY = 6
//    minimalna predkość to 1 kratka na sekunde, przy długości auta równej 4.5 m  to daje nam 16.2 km/h. Max velocity wtedy to 97.2 km/h
    }
}
