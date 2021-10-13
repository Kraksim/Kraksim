package pl.edu.agh.cs.kraksim.simulation.domain

import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

@Entity
class MapEntity(
    var type: MapType,
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    var roadNodes: List<RoadNodeEntity>,
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = [CascadeType.ALL])
    var roads: List<RoadEntity>,
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
    @LazyCollection(LazyCollectionOption.FALSE)
    var endingRoads: List<RoadEntity>,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    var startingRoads: List<RoadEntity>,
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    var turnDirections: List<TurnDirectionEntity>,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
}

@Entity
class RoadEntity(
    var length: Int,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var lanes: List<LaneEntity>,
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
