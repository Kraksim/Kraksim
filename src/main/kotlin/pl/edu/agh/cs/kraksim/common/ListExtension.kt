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
    if (this.isEmpty())
        emptyList()
    else
        withoutLast().zip(withoutFirst())

fun <K, V> Map<K, V>.getValues(keys: List<K>): List<V> {
    return keys.map { get(it)!! }
}

fun <T> List<T>.split() = Pair(first(), drop(1))

fun <T> Iterable<T>.takeEachWhile(predicate: (T) -> Boolean, action: (T) -> Unit) {
    for (item in this) {
        if (!predicate(item))
            break
        action(item)
    }
}

inline fun <reified FIRST : T, reified SECOND : T, T> Iterable<T>.partitionByType(): Triple<List<FIRST>, List<SECOND>, List<T>> {
    val first = ArrayList<FIRST>()
    val second = ArrayList<SECOND>()
    val rest = ArrayList<T>()
    for (element in this) {
        when (element) {
            is FIRST -> first.add(element)
            is SECOND -> second.add(element)
            else -> rest.add(element)
        }
    }
    return Triple(first, second, rest)
}

fun <T> Collection<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}

fun <T, K> Collection<T>.getRepeatsBy(keySelector: (T) -> K): Map<K, Int> {
    return groupingBy(keySelector).eachCount().filter { it.value > 1 }
}
