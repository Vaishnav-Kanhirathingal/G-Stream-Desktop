import java.awt.MouseInfo
import java.awt.Robot

val robot = Robot()

fun main() {
    robot.mouseMove(0, 0)
    animateToPosition(1920f, 1080f, 120f)
}

fun animateToPosition(dx: Float, dy: Float, frames: Float) {
    val x = MouseInfo.getPointerInfo().location.x.toFloat()
    val y = MouseInfo.getPointerInfo().location.y.toFloat()
    // TODO: perform within 5 ms
    println("x = $x,y = $y")
    for (i in 1..frames.toInt()) {
        Thread.sleep(30)
        val X = (x + (dx * i) / frames).toInt()
        val Y = (y + (dy * i) / frames).toInt()
        println("X = $X,Y = $Y")
        robot.mouseMove(X, Y)
    }
}