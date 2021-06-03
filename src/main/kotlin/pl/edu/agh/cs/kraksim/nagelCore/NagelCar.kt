package pl.edu.agh.cs.kraksim.nagelCore

class NagelCar(
    var positionRelativeToStart: Int = 0,
    var velocity: Int,
    var currentLane: NagelLane // todo zrobić na nullable
) {
    var distanceLeftToMove: Int = 0


    fun distanceFromRoadNode(): Int {
        return currentLane.length - positionRelativeToStart - 1
    }

    fun changeLane(lane: NagelLane, newPosition: Int) {
        currentLane.remove(this)
        currentLane = lane
        lane.addCar(this)
        positionRelativeToStart = newPosition
        distanceLeftToMove = 0
    }

    override fun toString(): String {
        return "NagelCar(positionRelativeToStart=$positionRelativeToStart, velocity=$velocity, distanceLeftToMove=$distanceLeftToMove)"
    }

}