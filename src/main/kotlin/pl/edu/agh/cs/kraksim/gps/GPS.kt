package pl.edu.agh.cs.kraksim.gps

import pl.edu.agh.cs.kraksim.core.state.Road
import pl.edu.agh.cs.kraksim.repository.entities.trafficState.GPSType

class GPS(
    val route: ArrayList<Road> = ArrayList(),
    val type: GPSType
) {

    constructor(vararg route: Road, type: GPSType) : this(ArrayList(route.map { it }), type)
    constructor(route: List<Road>, type: GPSType) : this(ArrayList(route), type)

    fun getNext(): Road =
        route.first()

    fun popNext(): Road =
        route.removeAt(0)
}
