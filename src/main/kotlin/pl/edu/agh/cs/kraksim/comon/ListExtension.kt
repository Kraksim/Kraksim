package pl.edu.agh.cs.kraksim.comon

fun <T> MutableList<T>.addToFront(element: T) {
    add(0, element)
}