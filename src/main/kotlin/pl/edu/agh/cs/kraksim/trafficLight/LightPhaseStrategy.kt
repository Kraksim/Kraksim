package pl.edu.agh.cs.kraksim.trafficLight

import pl.edu.agh.cs.kraksim.core.state.Intersection

interface LightPhaseStrategy {
    val id: Long

    fun initializeLights(intersections: Collection<Intersection>)
    fun switchLights(intersections: Collection<Intersection>)
}
