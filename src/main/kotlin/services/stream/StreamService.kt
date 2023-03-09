package services.stream

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skiko.toImage
import java.awt.Rectangle
import java.awt.Robot
import java.awt.image.BufferedImage
import java.io.DataOutputStream
import java.net.ServerSocket
import kotlin.random.Random

class StreamService(
    val audioServerError: () -> Unit,
    val videoServerError: () -> Unit
) {

    private val screenSocket = ServerSocket(0)
    private val audioSocket = ServerSocket(0)

    val screenPort get() = screenSocket.localPort
    val audioPort get() = audioSocket.localPort

    private var frames = 0

    init {
        val scope = CoroutineScope(Dispatchers.IO)
        // TODO: set screen resolution
        scope.launch { initiateVideoStreaming() }
        scope.launch { initiateAudioStreaming() }
        scope.launch {
            while (true) {
                Thread.sleep(1000)
                println("frames per second = $frames")
                frames = 0
            }
        }
    }

    private val ratio = 3
    private val inputWidth = 1920
    private val inputHeight = 1080

    private suspend fun initiateVideoStreaming() {
        val outputStream = DataOutputStream(screenSocket.accept().getOutputStream())
        val robot = Robot()
        try {
            while (true) {
                val fullScaleImage = robot.createScreenCapture(Rectangle(inputWidth, inputHeight))
                val bufferedImage = BufferedImage(inputWidth / ratio, inputHeight / ratio, BufferedImage.TYPE_INT_RGB)
                val graphics = bufferedImage.createGraphics()
                graphics.drawImage(fullScaleImage, 0, 0, inputWidth / ratio, inputHeight / ratio, null)
                graphics.dispose()
                val data = bufferedImage.toImage().encodeToData(EncodedImageFormat.JPEG, 60)
                val jpegByteArray = data?.bytes!!
                outputStream.apply {
                    frames++
                    writeInt(jpegByteArray.size)
                    write(jpegByteArray)
                    flush()
                }
            }
        } catch (e: Exception) {
            videoServerError()
            e.printStackTrace()
        }
    }

    private suspend fun initiateAudioStreaming() {
        val outputStream = DataOutputStream(audioSocket.accept().getOutputStream())
        try {
            while (true) {
                Thread.sleep(1000)
                val str = "random str ${Random(100).nextInt()}"
                println(str)
                outputStream.apply {
                    writeUTF(str)
                    flush()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            audioServerError()
        }
    }
}

/**
 * formats checked -
 * WebM
 * HLS
 * OBS (github)
 * MPEG-DASH (Native support on Android devices)
 * WebRTC (video conferencing protocol)
 * H.323 (sends audio and video data streams via the User Datagram Protocol (UDP) )
 * T.120 (covers communications over LAN)
 */