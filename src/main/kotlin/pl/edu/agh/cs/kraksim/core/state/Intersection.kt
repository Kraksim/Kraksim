package pl.edu.agh.cs.kraksim.core.state

import pl.edu.agh.cs.kraksim.trafficLight.TrafficLightPhase

interface Intersection : RoadNode {
    val id: Long
    val phases: Map<Lane, TrafficLightPhase>

    fun lightPhasesOf(road: Road): List<TrafficLightPhase>
}
