package services.stream

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        scope.launch { initiateVideoStreaming() }
        scope.launch { initiateAudioStreaming() }
    }

    private suspend fun initiateVideoStreaming() {
        val outputStream = DataOutputStream(screenSocket.accept().getOutputStream())
        var i = 0
        while (true) {
            outputStream.apply {
                i++
                writeUTF("i = $i")
                flush()
            }
            Thread.sleep(100)
            /**
             * capture a few consecutive frames with a specified interval
             * combine the captured frames into a packet using lossy compression
             * send those frames to the mobile device
             */
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