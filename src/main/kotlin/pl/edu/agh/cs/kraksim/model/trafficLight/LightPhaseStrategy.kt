package pl.edu.agh.cs.kraksim.model.trafficLight

import pl.edu.agh.cs.kraksim.model.movementSimulation.core.state.Intersection

interface LightPhaseStrategy {
    val id: Long

    fun initializeLights(intersections: Collection<Intersection>)
    fun switchLights(intersections: Collection<Intersection>)
}
