import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skiko.toImage
import java.awt.DisplayMode
import java.awt.GraphicsEnvironment
import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import javax.swing.JFrame

val robot = Robot()

val x = 800
val y = 600

fun setRes() {
    val frame = JFrame()
    frame.setSize(x, y)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    val graphics = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .defaultScreenDevice
    graphics.fullScreenWindow = frame
    graphics.displayMode = DisplayMode(x, y, 32, 60)
    frame.isVisible = true
}

@Composable
fun testImage() {
    MaterialTheme {
        Image(
            bitmap = (Robot().createScreenCapture(Rectangle(800, 600))).toComposeImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        )
    }
}

fun main() {
    val folder = File("imageGallery/")
    folder.mkdir()
    println("${folder.exists()} name - ${folder.absolutePath}")

    val time = System.currentTimeMillis()

    repeat(120) {
        val data = robot
            .createScreenCapture(Rectangle(1920, 1080))
            .toImage()
            .encodeToData(format = EncodedImageFormat.JPEG, quality = 10)
            ?.bytes
        val file = File(folder, "randomFile__$it.jpeg")
        file.writeBytes(data!!)
        println("${file.canWrite()} ${file.exists()} ${file.absolutePath}")
    }
    val total = System.currentTimeMillis() - time
    println("time taken = ${total}, mills per frame = ${total / 120}, frame rate = ${120000/total}")
}