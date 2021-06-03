package pl.edu.agh.cs.kraksim.nagelCore.simulation

import pl.edu.agh.cs.kraksim.comon.adjacentPairs
import pl.edu.agh.cs.kraksim.comon.random.RandomProvider
import pl.edu.agh.cs.kraksim.comon.withoutLast
import pl.edu.agh.cs.kraksim.core.SimulationState
import pl.edu.agh.cs.kraksim.nagelCore.*
import kotlin.math.min

class NagelMovementSimulationStrategy(
    private val random: RandomProvider
) {

    fun step(state: SimulationState) {
        acceleration(state)
        slowingDown(state)
        randomization(state)
        val carsToResolve = motion(state)
        resolveIntersections(carsToResolve)
    }

    fun acceleration(state: SimulationState) {
        state.cars
            .forEach { car ->
                if (car.velocity < MAX_VELOCITY)
                    car.velocity += 1
            }
    }

    fun slowingDown(state: SimulationState) {
        state.lanes.filter { it.containsCar() }
            .forEach { lane ->
                slowAllCarsButLast(lane.cars)
                slowLastCar(
                    endNode = lane.parentRoad.end(),
                    lane = lane,
                    lastCar = lane.cars.last()
                )
            }
    }

    private fun slowAllCarsButLast(cars: MutableList<NagelCar>) {
        cars.adjacentPairs().forEach { (car, carInFront) ->
            car.velocity = min(car.velocity, car.distanceFrom(carInFront))
        }
    }

    private fun slowLastCar(endNode: NagelRoadNode, lane: NagelLane, lastCar: NagelCar) {
        if (endNode is NagelIntersection) {
            // TODO zmienić wybór drogi
            val destinationLane = endNode.getPossibleRoads(lane)[0].lanes[0]

            val freeSpaceInCarPath =
                if (endNode.canGoThrough(lane)) {
                    lastCar.distanceFromRoadNode() + destinationLane.getFreeSpaceInFront()
                } else {
                    lastCar.distanceFromRoadNode()
                }

            lastCar.velocity = min(lastCar.velocity, freeSpaceInCarPath)
        }
    }

    //TODO refactor and write tests
    fun randomization(state: SimulationState) {
        state.cars
            .forEach { car ->
                val shouldSlowDown = car.velocity > 0 && random.getBoolean(SLOW_DOWN_PROBABILITY)
                if (shouldSlowDown) {
                    car.velocity -= 1
                }
            }
    }

    //TODO refactor and write tests
    fun motion(state: SimulationState): HashMap<NagelLane, ArrayList<NagelCar>> {
        val carsToResolve = HashMap<NagelLane, ArrayList<NagelCar>>()

        state.lanes.filter { it.containsCar() }
            .forEach {

                it.cars.withoutLast().forEach { car ->
                    car.positionRelativeToStart += car.velocity
                    // todo pamietać o pasach ze się mogą skończyć
                }

                val lastCar = it.cars.last()
                // todo ten lane musi być taki sam co w fazie slowing down - uwzględnić

                val endNode = it.parentRoad.end()
                val distanceFromIntersection = lastCar.distanceFromRoadNode()
                val distanceToMove = min(distanceFromIntersection, lastCar.velocity)
                lastCar.positionRelativeToStart += distanceToMove
                lastCar.distanceLeftToMove = lastCar.velocity - distanceToMove

                when (endNode) {
                    is NagelIntersection -> {
                        val lane = endNode.getPossibleRoads(it)[0].lanes[0]
                        if (lastCar.distanceLeftToMove != 0) {
                            val cars: ArrayList<NagelCar> = carsToResolve[lane] ?: ArrayList()
                            cars.add(lastCar)
                            carsToResolve[lane] = cars
                        }
                    }
                    is NagelGateway -> {
                        endNode.addCar(lastCar)
                        lastCar.currentLane!!.remove(lastCar)
                    }
                }


            }

        return carsToResolve
    }

    //TODO refactor and write tests
    fun resolveIntersections(carsToResolve: HashMap<NagelLane, ArrayList<NagelCar>>) {
        carsToResolve.forEach {
            val lane = it.key
            val carsTryingToGetToLane =
                it.value.sortedWith(compareByDescending { car -> car.distanceLeftToMove }).toMutableList()

            val firstCarInNextLane = lane.cars.getOrNull(0)
            var spaceLeft = firstCarInNextLane?.positionRelativeToStart ?: lane.cellsCount

            while (carsTryingToGetToLane.isNotEmpty() && spaceLeft > 0) {
                val currentCar = carsTryingToGetToLane.removeAt(0)
                val newPosition = min(spaceLeft - 1, currentCar.distanceLeftToMove)

                currentCar.moveToLane(lane, newPosition)
                spaceLeft = newPosition
            }
        }
    }

    companion object {
        const val SLOW_DOWN_PROBABILITY = 0.5
        const val MAX_VELOCITY = 6
//    minimalna predkość to 1 kratka na sekunde, przy długości auta równej 4.5 m  to daje nam 16.2 km/h. Max velocity wtedy to 97.2 km/h
    }
}