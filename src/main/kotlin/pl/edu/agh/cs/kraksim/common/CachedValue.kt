package pl.edu.agh.cs.kraksim.common

class CachedValue<BY : Any, VAL : Any>(
    private val populator: (BY) -> VAL
) {

    private var cachedBy: BY? = null
    private lateinit var cachedValue: VAL

    fun get(by: BY): VAL {
        if (cachedBy != by) {
            cachedBy = by
            cachedValue = populator(by)
            return cachedValue
        }
        return cachedValue
    }
}
