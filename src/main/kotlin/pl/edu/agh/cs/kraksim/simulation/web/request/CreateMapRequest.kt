package pl.edu.agh.cs.kraksim.simulation.web.request

import pl.edu.agh.cs.kraksim.simulation.domain.MapType
import pl.edu.agh.cs.kraksim.simulation.domain.RoadNodeType
import javax.validation.constraints.NotBlank

class CreateMapRequest(
    var type: MapType,
    var roadNodes: List<CreateRoadNodeRequest>,
    var roads: List<CreateRoadRequest>,
    @field:NotBlank
    var name: String,
)

class CreateRoadNodeRequest(
    var type: RoadNodeType,
    var position: CreatePositionRequest,
    var endingRoadsIds: List<Long>,
    var startingRoadsIds: List<Long>,
    var turnDirections: List<CreateTurnDirectionRequest>,
    @field:NotBlank
    var name: String,
)

class CreateRoadRequest(
    var length: Int,
    var lanes: List<CreateLaneRequest>,
    var id: Long = 0,
    @field:NotBlank
    var name: String,
)

class CreateLaneRequest(
    var startingPoint: Int,
    var endingPoint: Int,
    var indexFromLeft: Int,
    @field:NotBlank
    var id: Long = 0
)

class CreateTurnDirectionRequest(
    var sourceLaneId: Long,
    var destinationRoadId: Long
)

data class CreatePositionRequest(var x: Double, var y: Double)
