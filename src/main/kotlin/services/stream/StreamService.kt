package services.stream

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Robot
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
        val robot = Robot()
        while (true) {
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
 */