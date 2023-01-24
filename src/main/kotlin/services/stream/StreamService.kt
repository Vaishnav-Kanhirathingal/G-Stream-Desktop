package services.stream

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skiko.toImage
import java.awt.Rectangle
import java.awt.Robot
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.DataOutputStream
import java.net.ServerSocket

class StreamService {
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

    private val outPutWidth = 1920/3
    private val outPutHeight = 1080/3

    private suspend fun initiateVideoStreaming() {
        val outputStream = DataOutputStream(screenSocket.accept().getOutputStream())
        val robot = Robot()
        while (true) {

            //---------------------------------------------------------------------------------------------------------||
            val fullScaleImage = robot.createScreenCapture(Rectangle(1920, 1080))

            //---------------------------------------------------------------------------------------------------------24Max
            val bufferedImage1 =
                AffineTransformOp(AffineTransform.getScaleInstance(0.5, 0.5), AffineTransformOp.TYPE_BILINEAR).filter(
                    fullScaleImage, BufferedImage(outPutWidth, outPutHeight, fullScaleImage.type)
                )

            //---------------------------------------------------------------------------------------------------------25Max
            val bufferedImage2 = BufferedImage(outPutWidth, outPutHeight, BufferedImage.TYPE_INT_ARGB)
            val graphics = bufferedImage2.createGraphics()
            graphics.drawImage(fullScaleImage, 0, 0, outPutWidth, outPutHeight, null)
            graphics.dispose()

            val data = bufferedImage2.toImage().encodeToData(EncodedImageFormat.JPEG, 60)
            val jpegByteArray = data?.bytes!!

            //---------------------------------------------------------------------------------------------------------||
            outputStream.apply {
                frames++
                writeInt(jpegByteArray.size)
                write(jpegByteArray)
                flush()
            }
            // TODO: capture a few consecutive frames with a specified interval combine the captured frames into a
            //  packet using lossy compression send those frames to the mobile device
        }
    }

    private suspend fun initiateAudioStreaming() {
        // TODO: keep streaming audio
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