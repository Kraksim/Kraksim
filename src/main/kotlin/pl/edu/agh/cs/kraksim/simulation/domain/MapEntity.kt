package pl.edu.agh.cs.kraksim.simulation.domain

import pl.edu.agh.cs.kraksim.common.converter.MovementEnumToStringConverter
import pl.edu.agh.cs.kraksim.trafficState.domain.entity.MovementSimulationStrategyType
import javax.persistence.*

@Entity
class MapEntity(
    var type: MapType,
    @OneToMany(cascade = [CascadeType.ALL])
    var roadNodes: List<RoadNodeEntity>,
    @OneToMany(cascade = [CascadeType.ALL])
    var roads: List<RoadEntity>,
    var name: String = "",
    @Column
    @Convert(converter = MovementEnumToStringConverter::class)
    var compatibleWith: List<MovementSimulationStrategyType>,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class RoadNodeEntity(
    var type: RoadNodeType,
    @Embedded
    var position: PositionEntity,
    @OneToMany(cascade = [CascadeType.ALL])
    var endingRoads: List<RoadEntity>,
    @OneToMany(cascade = [CascadeType.ALL])
    var startingRoads: List<RoadEntity>,
    @OneToMany(cascade = [CascadeType.ALL])
    var turnDirections: List<TurnDirectionEntity>,
    var name: String = "",
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class RoadEntity(
    var length: Int,
    @OneToMany(cascade = [CascadeType.ALL])
    var lanes: List<LaneEntity>,
    var name: String = "",
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class LaneEntity(
    var startingPoint: Int,
    var endingPoint: Int,
    var indexFromLeft: Int,
    var name: String = "",
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class TurnDirectionEntity(
    @ManyToOne
    var sourceLane: LaneEntity,
    @ManyToOne
    var destinationRoad: RoadEntity
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

enum class MapType {
    MAP, NO_MAP
}

enum class RoadNodeType {
    INTERSECTION, GATEWAY
}

@Embeddable
class PositionEntity(var x: Double, var y: Double)
