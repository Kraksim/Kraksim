package pl.edu.agh.cs.kraksim.gps

import pl.edu.agh.cs.kraksim.core.state.Road

class GPS(
    val route: ArrayList<Road> = ArrayList()
) {

    constructor(vararg route: Road) : this(ArrayList(route.map { it }))
    constructor(route: List<Road>) : this(ArrayList(route))

    fun getNext(): Road =
        route.first()

    fun popNext(): Road =
        route.removeAt(0)
}
