package pl.edu.agh.cs.kraksim.common.gps

import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.gps.GPS

class SetMockRoadGps(override val route: ArrayList<Road>) : GPS {

    constructor(vararg route: Road) : this(ArrayList(route.map { it }))

    override fun getNext(): Road =
        route.first()

    override fun popNext(): Road =
        route.removeAt(0)
}
