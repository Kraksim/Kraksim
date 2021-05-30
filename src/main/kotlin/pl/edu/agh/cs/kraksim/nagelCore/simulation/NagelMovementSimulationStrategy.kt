package pl.edu.agh.cs.kraksim.nagelCore.simulation

import pl.edu.agh.cs.kraksim.core.SimulationState
import pl.edu.agh.cs.kraksim.nagelCore.NagelCar
import pl.edu.agh.cs.kraksim.nagelCore.NagelGateway
import pl.edu.agh.cs.kraksim.nagelCore.NagelIntersection
import pl.edu.agh.cs.kraksim.nagelCore.NagelLane
import kotlin.math.min

class NagelMovementSimulationStrategy {

    fun step(state: SimulationState) {
        acceleration(state)
        slowingDown(state)
        randomization(state)
        val carsToResolve = motion(state)
        resolveIntersections(carsToResolve)

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

                val lastCar = it.cars[it.cars.size - 1]

                val endNode = it.parentRoad.end
                if (endNode.canEnterNodeFrom(it)) {
                    // todo ostatnie auto przejazd przez skrzyżowanie (gdzie i kiedy?)
                    // todo zmienić wybór drogi

                    if (endNode is NagelIntersection) {
                        val lane = endNode.getPossibleRoads(it)[0].lanes[0]
                        val firstCarInNextLane: NagelCar? = lane.cars.getOrNull(0)
                        val distanceFromIntersection = lastCar.distanceFromRoadNode()
                        val spaceInDestinationLane = firstCarInNextLane?.positionRelativeToStart ?: it.length
                        val distanceFromCarInFront = distanceFromIntersection + spaceInDestinationLane

                        if (lastCar.velocity > distanceFromCarInFront) {
                            lastCar.velocity = distanceFromCarInFront
                        }

                    }

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

    private fun motion(state: SimulationState): HashMap<NagelLane, ArrayList<NagelCar>> {
        val carsToResolve = HashMap<NagelLane, ArrayList<NagelCar>>()

        state.roads.flatMap { it.lanes }
            .forEach {
                for ((index, car) in it.cars.withIndex()) {
                    if (index == it.cars.size - 1) {
                        continue
                    }
                    car.positionRelativeToStart += car.velocity
                    // todo pamietać o pasach ze się mogą skończyć
                }

                val lastCar = it.cars[it.cars.size - 1]
                // todo ten lane musi być taki sam co w fazie slowing down - uwzględnić


                val endNode = it.parentRoad.end
                val distanceFromIntersection = lastCar.distanceFromRoadNode()
                lastCar.positionRelativeToStart + distanceFromIntersection
                lastCar.distanceLeftToMove = lastCar.velocity - distanceFromIntersection

                when (endNode) {
                    is NagelIntersection -> {
                        val lane = endNode.getPossibleRoads(it)[0].lanes[0]
                        if (lastCar.distanceLeftToMove != 0) {
                            val cars: ArrayList<NagelCar> = carsToResolve[it] ?: ArrayList()
                            cars.add(lastCar)
                            carsToResolve[lane] = cars
                        }
                    }
                    is NagelGateway -> {
                        endNode.addCar(lastCar)
                        lastCar.currentLane.remove(lastCar)
                    }
                }


            }

        return carsToResolve
    }

    private fun resolveIntersections(carsToResolve: HashMap<NagelLane, ArrayList<NagelCar>>) {
        carsToResolve.forEach {
            val lane = it.key
            val carsTryingToGetToLane =
                it.value.sortedWith(compareByDescending { car -> car.distanceLeftToMove }).toMutableList()

            val firstCarInNextLane = lane.cars.getOrNull(0)
            var spaceLeft = firstCarInNextLane?.positionRelativeToStart ?: lane.length

            while (carsTryingToGetToLane.isNotEmpty() || spaceLeft > 0) {
                val currentCar = carsTryingToGetToLane.removeAt(0)
                val newPosition = min(spaceLeft - 1, currentCar.distanceLeftToMove)

                currentCar.changeLane(lane, newPosition)
                currentCar.velocity = min(currentCar.velocity, spaceLeft)
                spaceLeft = newPosition
            }
        }
    }

    companion object {
        const val RANDOM_PROPABILITY = 0.5
        const val MAX_VELOCITY = 6
//    minimalna predkość to 1 kratka na sekunde, przy długości auta równej 4.5 m  to daje nam 16.2 km/h. Max velocity wtedy to 97.2 km/h
    }
}