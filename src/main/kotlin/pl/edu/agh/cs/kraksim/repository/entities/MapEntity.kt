package pl.edu.agh.cs.kraksim.repository.entities

import pl.edu.agh.cs.kraksim.common.RoadId
import pl.edu.agh.cs.kraksim.repository.LongArrayToStringConverter
import javax.persistence.*

@Entity
class MapEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    var type: MapType,
    @OneToMany
    var roadNodes: List<RoadNodeEntity>,
    @OneToMany
    var roads: List<RoadEntity>,
)

@Entity
class RoadNodeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    var type: RoadNodeType,
    @Embedded
    var position: PositionEntity,
    @Column
    @Convert(converter = LongArrayToStringConverter::class)
    var endingRoads: List<RoadId>,
    @Column
    @Convert(converter = LongArrayToStringConverter::class)
    var startingRoads: List<RoadId>,
    @OneToMany
    var turnDirections: List<TurnDirectionEntity>,

)

@Entity
class RoadEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    var length: Int,
    @OneToMany
    var lanes: List<LaneEntity>,
)

@Entity
class LaneEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    var startingPoint: Int,
    var endingPoint: Int,
    var indexFromLeft: Int,
)

@Entity
class TurnDirectionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    var sourceLaneId: Long,
    var destinationRoadId: Long
)

enum class MapType {
    MAP, NO_MAP
}

enum class RoadNodeType {
    INTERSECTION, GATEWAY
}

@Embeddable
class PositionEntity(x: Double, y: Double)
