package services.stream

import androidx.compose.foundation.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skiko.toImage
import java.awt.Rectangle
import java.awt.Robot
import java.io.DataOutputStream
import java.net.ServerSocket

// TODO: try implementing video conferencing app

class StreamService {
    private val screenSocket = ServerSocket(0)
    private val audioSocket = ServerSocket(0)

    val screenPort get() = screenSocket.localPort
    val audioPort get() = audioSocket.localPort

    init {
        val scope = CoroutineScope(Dispatchers.IO)
        // TODO: set screen resolution
        scope.launch { initiateVideoStreaming() }
        scope.launch { initiateAudioStreaming() }
    }

    private suspend fun initiateVideoStreaming() {
        val outputStream = DataOutputStream(screenSocket.accept().getOutputStream())
        var i = 0
        val robot = Robot()
        while (true) {

            //---------------------------------------------------------------------------------------------------------||

            val data = robot
                .createScreenCapture(Rectangle(384, 216))
                .toImage()
                .encodeToData(EncodedImageFormat.JPEG, 10)
            val jpegByteArray = data?.bytes!!

            //---------------------------------------------------------------------------------------------------------||
            outputStream.apply {
                i++
                writeInt(jpegByteArray.size)
                write(jpegByteArray)
                flush()
            }
            println("frame sent - $i")
            Thread.sleep(1000)
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