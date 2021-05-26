package pl.edu.agh.cs.kraksim.comon

class TrafficLightPhase(
    var phaseTime: Int,
    var state: LightColor
) {


    enum class LightColor {
        RED, GREEN
    }
}
