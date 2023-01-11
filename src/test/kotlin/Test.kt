import java.awt.MouseInfo
import java.awt.Robot
import java.time.LocalDateTime

val robot = Robot()

fun main(args: Array<String>) {
    Thread.sleep(10000)
    for (i in 0..10000 step 50) {
        Thread.sleep(20)
        robot.mouseMove(
            i,
            MouseInfo.getPointerInfo().location.y
        )//moves mouse to right by 100px
    }
}

fun testMouseDelta(dx: Int, dy: Int) {
    robot.mouseMove(
        MouseInfo.getPointerInfo().location.x + dx,
        MouseInfo.getPointerInfo().location.y + dy
    )
}

fun testInput() {
    val currentTime = LocalDateTime.now().second
    while (LocalDateTime.now().second < currentTime + 6) {
        try {
            robot.mouseMove(960 + 20, 540)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun monitorMouseInput() {
    repeat(1000) {
        Thread.sleep(1000)
        println("x - ${MouseInfo.getPointerInfo().location.x} \ty - ${MouseInfo.getPointerInfo().location.y}")
    }
}

fun testSmall() {
    Thread.sleep(3000)
    val currentTime = LocalDateTime.now().second
    while (LocalDateTime.now().second < currentTime + 6) {
        try {
            robot.mouseMove(960 + 1, 539)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
