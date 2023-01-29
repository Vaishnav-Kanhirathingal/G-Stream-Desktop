package services.control

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import services.control.data.JoyStickControls
import services.control.data.MouseData
import services.control.data.PadControls
import java.io.DataInputStream
import java.net.ServerSocket

/**
 * @param movementServerError executed after movement server error
 * @param gamePadServerError executed after gamePad server error
 * @param mouseTrackServerError executed after mouseTrack server error
 * @param leftGamePadServerError executed after leftGamePad server error
 */
class ControlService(
    private val movementServerError: () -> Unit,
    private val gamePadServerError: () -> Unit,
    private val mouseTrackServerError: () -> Unit,
    private val leftGamePadServerError: () -> Unit,
) {
    private val leftGamePadServer = ServerSocket(0)
    private val movementServer = ServerSocket(0)
    private val gamePadServer = ServerSocket(0)
    private val mouseTrackServer = ServerSocket(0)

    val leftGamePadPort get() = leftGamePadServer.localPort
    val movementPort get() = movementServer.localPort
    val gamePadPort get() = gamePadServer.localPort
    val mouseTrackPort get() = mouseTrackServer.localPort

    init {
        CoroutineScope(Dispatchers.IO).apply {
            launch { initiateMovementServer() }
            launch { initiateGamePadServer() }
            launch { initiateMouseTrackServer() }
            launch { initiateLeftGamePadServer() }
        }
    }

    private suspend fun initiateMovementServer() {
        val inputStream = DataInputStream(movementServer.accept().getInputStream())
        try {
            while (true) {
                val string = inputStream.readUTF()
                PerformActions.performMovementAction(Gson().fromJson(string, JoyStickControls::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace();movementServerError()
        }
    }

    private suspend fun initiateGamePadServer() {
        val inputStream = DataInputStream(gamePadServer.accept().getInputStream())
        try {
            while (true) {
                val string = inputStream.readUTF()
                PerformActions.performRightGamePadAction(Gson().fromJson(string, PadControls::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace();gamePadServerError()
        }
    }

    private suspend fun initiateMouseTrackServer() {
        val inputStream = DataInputStream(mouseTrackServer.accept().getInputStream())
        try {
            while (true) {
                val string = inputStream.readUTF()
                PerformActions.performMouseTrackAction(Gson().fromJson(string, MouseData::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace();mouseTrackServerError()
        }
    }

    private suspend fun initiateLeftGamePadServer() {
        val inputStream = DataInputStream(leftGamePadServer.accept().getInputStream())
        try {
            while (true) {
                val string = inputStream.readUTF()
                PerformActions.performLeftGamePadAction(Gson().fromJson(string, PadControls::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace();leftGamePadServerError()
        }
    }
}