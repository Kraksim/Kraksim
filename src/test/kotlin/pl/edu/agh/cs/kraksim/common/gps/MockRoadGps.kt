package pl.edu.agh.cs.kraksim.common.gps

import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.gps.GPS

class MockRoadGps : GPS {

    override val route = ArrayList<Road>()

    override fun getNext(): Road =
        throw IllegalStateException("Function shouldn't be called")


    override fun popNext(): Road =
        throw IllegalStateException("Function shouldn't be called")
}
