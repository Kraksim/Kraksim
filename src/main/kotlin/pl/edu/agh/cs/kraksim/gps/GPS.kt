package pl.edu.agh.cs.kraksim.gps

import pl.edu.agh.cs.kraksim.common.CachedValue
import pl.edu.agh.cs.kraksim.common.Direction
import pl.edu.agh.cs.kraksim.common.LaneId
import pl.edu.agh.cs.kraksim.core.state.Intersection
import pl.edu.agh.cs.kraksim.core.state.Lane
import pl.edu.agh.cs.kraksim.core.state.Road
import kotlin.math.abs

class GPS(
    val route: ArrayList<Road>,
    val type: GPSType
) {
    constructor(vararg route: Road, type: GPSType) : this(ArrayList(route.map { it }), type)
    constructor(route: List<Road>, type: GPSType) : this(ArrayList(route), type)

    lateinit var currentRoad: Road
    private val cachedChangeLaneDirection: CachedValue<Lane, Direction> =
        CachedValue(this::calculateChangeLaneDirection)
    private var cachedTargetLaneInNextRoad: CachedValue<Road, Lane>? = null

    fun popNext(): Road {
        currentRoad = route.removeAt(0)
        return currentRoad
    }

    fun getNext(): Road =
        route.first()

    fun getChangeLaneDirection(currentLane: Lane): Direction {
        return cachedChangeLaneDirection.get(currentLane)
    }

    fun getTargetLaneInNextRoad(laneProvider: (Road) -> Lane): Lane {
        if (cachedTargetLaneInNextRoad == null) {
            cachedTargetLaneInNextRoad = CachedValue(laneProvider)
        }
        return cachedTargetLaneInNextRoad!!.get(getNext())
    }

    private fun calculateChangeLaneDirection(currentLane: Lane): Direction {
        val difference = distanceToTargetLane(currentLane)
        return Direction.fromDistanceToTargetLane(difference)
    }

    private fun distanceToTargetLane(
        currentLane: Lane
    ): Int {
        val approachingRoadNode = currentRoad.end
        if (approachingRoadNode is Intersection) {
            val destinationRoadId = getNext().id
            val lanesLeadingToDestinationRoad: Set<LaneId> = approachingRoadNode.getLanesLeadingTo(destinationRoadId)
            return currentRoad.lanes.filter { lanesLeadingToDestinationRoad.contains(it.id) }
                .map { potentialLane -> potentialLane.indexFromLeft - currentLane.indexFromLeft }
                .minByOrNull { abs(it) } ?: 0
        }
        return 0
    }
}
