package pl.edu.agh.cs.kraksim.gps

import pl.edu.agh.cs.kraksim.core.state.Road

interface GPS {
    val route: ArrayList<Road>

    fun getNext(): Road
    fun popNext(): Road
}
