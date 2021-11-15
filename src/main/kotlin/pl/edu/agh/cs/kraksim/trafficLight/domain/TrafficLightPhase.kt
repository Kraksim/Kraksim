package pl.edu.agh.cs.kraksim.trafficLight.domain

class TrafficLightPhase(
    var phaseTime: Int = 0,
    var state: LightColor = LightColor.RED,
    var period: Int? = null
) {

    fun switchLight(period: Int) {
        this.period = period
        phaseTime = 0
        state = when (state) {
            LightColor.RED -> LightColor.GREEN
            LightColor.GREEN -> LightColor.RED
        }
    }

    override fun toString(): String {
        return "TrafficLightPhase(phaseTime=$phaseTime, period=$period, state=$state)"
    }

    enum class LightColor {
        RED, GREEN
    }
}
