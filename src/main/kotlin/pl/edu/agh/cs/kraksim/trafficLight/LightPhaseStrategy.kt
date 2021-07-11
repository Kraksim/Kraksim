package pl.edu.agh.cs.kraksim.trafficLight

import pl.edu.agh.cs.kraksim.core.state.Intersection

interface LightPhaseStrategy {

    fun initializeLights(intersections: Collection<Intersection>)
    fun switchLights(intersections: Collection<Intersection>)
}
