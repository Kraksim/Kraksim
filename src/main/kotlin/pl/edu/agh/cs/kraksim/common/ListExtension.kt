package pl.edu.agh.cs.kraksim.common

fun <T> MutableList<T>.addToFront(element: T) {
    add(0, element)
}

fun <T> MutableList<T>.withoutLast() =
    dropLast(1)

fun <T> MutableList<T>.withoutFirst() =
    drop(1)

/**
 * returns collection consisting neighbouring elements
 * e.g. given list [1, 2, 3, 4, 5] function will return
 * [(1, 2), (2, 3), (3, 4), (4, 5)]
 */
fun <T> MutableList<T>.adjacentPairs(): List<Pair<T, T>> =
    withoutLast().zip(withoutFirst())

fun <K, V> Map<K, V>.getValues(keys: List<K>): List<V> {
    return keys.map { get(it)!! }
}
