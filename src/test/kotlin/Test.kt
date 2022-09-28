import java.awt.MouseInfo
import java.awt.Robot

var pastTime: Long? = null

fun main() {
    for (i in 0..10) {
        val time = System.currentTimeMillis()
        val gap = time - (pastTime ?: time)
        pastTime = time
        Thread.sleep(40)
        val dx = (-400..400).random()
        val dy = (-400..400).random()
        animateToPosition(
            dx = dx,
            dy = dy,
            timeGap = gap
        )
    }
}

val robot = Robot()

private fun animateToPosition(
    dx: Int,
    dy: Int,
    frames: Int = 5,
    timeGap: Long
) {
    val duration = if (timeGap > 50) 50 else timeGap
    val perFrameTiming = duration / frames
    println("perFrameTiming : $perFrameTiming, \t\ttimeGap : $timeGap, \t\tdx : $dx,\t\tdy : $dy")
    val x = MouseInfo.getPointerInfo().location.x.toFloat()
    val y = MouseInfo.getPointerInfo().location.y.toFloat()
    for (i in 1..frames) {
        Thread.sleep(perFrameTiming)
        val fx = (x + (dx * i) / frames).toInt()
        val fy = (y + (dy * i) / frames).toInt()
        robot.mouseMove(fx, fy)
    }
}