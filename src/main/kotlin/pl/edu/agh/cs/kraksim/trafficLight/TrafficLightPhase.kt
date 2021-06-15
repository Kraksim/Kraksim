package pl.edu.agh.cs.kraksim.trafficLight

class TrafficLightPhase(
    var phaseTime: Int = 0,
    var state: LightColor = LightColor.RED
) {

    fun switchLight(phaseTime: Int = this.phaseTime) {
        this.phaseTime = phaseTime
        state = when (state) {
            LightColor.RED -> LightColor.GREEN
            LightColor.GREEN -> LightColor.RED
        }
    }

    override fun toString(): String {
        return "TrafficLightPhase(phaseTime=$phaseTime, state=$state)"
    }

    enum class LightColor {
        RED, GREEN
    }
}
