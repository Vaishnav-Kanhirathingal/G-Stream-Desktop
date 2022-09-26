import java.awt.Robot
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun main() {
    controllerParamToPositionParam(1, 90)
}

private fun controllerParamToPositionParam(angle: Int, strength: Int) {
    val dx = sin(Math.toRadians(angle.toDouble())) * strength
    val dy = cos(Math.toRadians(angle.toDouble())) * strength
    println("dx = $dx, dy = $dy")
    moveTo(1920, 1080)
    moveTo(-1920, -1080)
    moveTo(0, 0)
}

val r = Robot()
var x1 = 0
var y1 = 0

private fun moveTo(dx: Int, dy: Int) {
    val pixelJump = 2

    val x2 = x1 + dx
    val y2 = y1 + dy

    val lineLength = sqrt(((dx * dx) + (dy * dy)).toDouble())
    var dt = 0.0
    while (dt < lineLength) {
        dt += pixelJump.toDouble()
        val t = dt / lineLength
        val dx = ((1 - t) * x1 + t * x2).toInt()
        val dy = ((1 - t) * y1 + t * y2).toInt()
        r.mouseMove(dx, dy)
        r.delay(1)
    }
    r.mouseMove(x2, y2)

    x1=x2
    y1=y2
}