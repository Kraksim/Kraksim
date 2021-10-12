package pl.edu.agh.cs.kraksim.controller.dto

import pl.edu.agh.cs.kraksim.repository.entities.MapType
import pl.edu.agh.cs.kraksim.repository.entities.RoadNodeType

class MapDTO(
    var type: MapType,
    var roadNodes: List<RoadNodeDTO>,
    var roads: List<RoadDTO>,
) {
    var id: Long = 0
}

class RoadNodeDTO(
    var type: RoadNodeType,
    var position: PositionDTO,
    var endingRoads: List<RoadDTO>,
    var startingRoads: List<RoadDTO>,
    var turnDirections: List<TurnDirectionDTO>,
) {
    var id: Long = 0
}

class RoadDTO(
    var length: Int,
    var lanes: List<LaneDTO>,
) {
    var id: Long = 0
}

class LaneDTO(
    var startingPoint: Int,
    var endingPoint: Int,
    var indexFromLeft: Int,
) {
    var id: Long = 0
}

class TurnDirectionDTO(
    var sourceLane: LaneDTO,
    var destinationRoad: RoadDTO
) {
    var id: Long = 0
}

data class PositionDTO(var x: Double, var y: Double)
