package pl.edu.agh.cs.kraksim.common

inline fun <T, R : Comparable<R>> HashSet<T>.popMinBy(selector: (T) -> R): T {
    val value = minByOrNull { selector(it) } ?: throw NullPointerException("popMinBy on empty set")
    remove(value)
    return value
}
