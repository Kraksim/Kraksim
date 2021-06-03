package pl.edu.agh.cs.kraksim.comon

fun <T> MutableList<T>.addToFront(element: T) {
    add(0, element)
}

fun <T> MutableList<T>.withoutLast() =
    dropLast(1)


fun <T> MutableList<T>.withoutFirst() =
    drop(1)

fun <T> MutableList<T>.adjacentPairs(): List<Pair<T, T>> =
    withoutLast().zip(withoutFirst())

