package pl.edu.agh.cs.kraksim.simulation.domain

import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType

class MapDTO(
    var type: MapType,
    var roadNodes: List<RoadNodeDTO>,
    var roads: List<RoadDTO>,
    var name: String,
    var compatibleWith: List<MovementSimulationStrategyType>,
) {
    var id: Long = 0
}

class BasicMapInfoDTO(
    var type: MapType,
    var name: String,
    var id: Long = 0,
    var nodes: List<BasicRoadNodeDto>,
    var edges: List<BasicEdgeDto>

)

class BasicEdgeDto(
    var from: Long,
    var to: Long,
    var roadThickness: Int
)

class BasicRoadNodeDto(
    var name: String,
    var type: RoadNodeType,
    var position: PositionDTO,
    var id: Long = 0
)

class RoadNodeDTO(
    var type: RoadNodeType,
    var position: PositionDTO,
    var endingRoads: List<RoadDTO>,
    var startingRoads: List<RoadDTO>,
    var turnDirections: List<TurnDirectionDTO>,
    var name: String,
) {
    var id: Long = 0
}

class RoadDTO(
    var length: Int,
    var lanes: List<LaneDTO>,
    var name: String,
) {
    var id: Long = 0
}

class LaneDTO(
    var startingPoint: Int,
    var endingPoint: Int,
    var indexFromLeft: Int,
    var name: String,
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
