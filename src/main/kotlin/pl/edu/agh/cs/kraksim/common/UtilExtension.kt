package pl.edu.agh.cs.kraksim.common

import java.util.concurrent.ExecutionException
import kotlin.random.Random

inline fun <T, R : Comparable<R>> HashSet<T>.popMinBy(selector: (T) -> R): T {
    val value = minByOrNull { selector(it) } ?: throw NullPointerException("popMinBy on empty set")
    remove(value)
    return value
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

@JvmName("averageOfIntOr")
fun Iterable<Int>.averageOr(default: Double) = average().takeIf { !it.isNaN() } ?: default

@JvmName("averageOfDoubleOr")
fun Iterable<Double>.averageOr(default: Double) = average().takeIf { !it.isNaN() } ?: default

fun Random.nextPositiveLong() = nextLong(from = 0L, until = Long.MAX_VALUE)

inline fun whileWithTimeout(timeoutMilliseconds: Long, block: () -> Unit) {
    val start = System.currentTimeMillis()
    while (System.currentTimeMillis() - start < timeoutMilliseconds) {
        block()
    }
}

inline fun <T> unwrapExecutionException(function: () -> Nothing): T {
    try {
        function()
    } catch (ex: ExecutionException) {
        val cause = ex.cause ?: ex
        throw cause
    }
}
