package pl.edu.agh.cs.kraksim.simulation.web.request

import pl.edu.agh.cs.kraksim.simulation.domain.MapType
import pl.edu.agh.cs.kraksim.simulation.domain.RoadNodeType

class CreateMapRequest(
    var type: MapType,
    var roadNodes: List<CreateRoadNodeRequest>,
    var roads: List<CreateRoadRequest>,
)

class CreateRoadNodeRequest(
    var type: RoadNodeType,
    var position: CreatePositionRequest,
    var endingRoadsIds: List<Long>,
    var startingRoadsIds: List<Long>,
    var turnDirections: List<CreateTurnDirectionRequest>,
)

class CreateRoadRequest(
    var length: Int,
    var lanes: List<CreateLaneRequest>,
    var id: Long = 0
)

class CreateLaneRequest(
    var startingPoint: Int,
    var endingPoint: Int,
    var indexFromLeft: Int,
    var id: Long = 0
)

class CreateTurnDirectionRequest(
    var sourceLaneId: Long,
    var destinationRoadId: Long
)

data class CreatePositionRequest(var x: Double, var y: Double)