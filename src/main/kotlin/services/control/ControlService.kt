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

class ControlService {
    private val movementServer = ServerSocket(0)
    private val gamePadServer = ServerSocket(0)
    private val mouseTrackServer = ServerSocket(0)
    private val shiftServer = ServerSocket(0)

    val movementPort get() = movementServer.localPort
    val gamePadPort get() = gamePadServer.localPort
    val mouseTrackPort get() = mouseTrackServer.localPort
    val shiftPort get() = shiftServer.localPort

    init {
        CoroutineScope(Dispatchers.IO).apply {
            launch { initiateMovementServer() }
            launch { initiateGamePadServer() }
            launch { initiateMouseTrackServer() }
            launch { initiateShiftServer() }
        }
    }

    private suspend fun initiateMovementServer() {
        val inputStream = DataInputStream(movementServer.accept().getInputStream())
        while (true) {
            val string = inputStream.readUTF()
            PerformActions.performMovementAction(Gson().fromJson(string, JoyStickControls::class.java))
        }
    }

    private suspend fun initiateGamePadServer() {
        val inputStream = DataInputStream(gamePadServer.accept().getInputStream())
        while (true) {
            val string = inputStream.readUTF()
            PerformActions.performGamePadAction(Gson().fromJson(string, PadControls::class.java))
        }
    }

    private suspend fun initiateMouseTrackServer() {
        val inputStream = DataInputStream(mouseTrackServer.accept().getInputStream())
        while (true) {
            val string = inputStream.readUTF()
            PerformActions.performMouseTrackAction(Gson().fromJson(string, MouseData::class.java))
        }
    }

    private suspend fun initiateShiftServer() {
        val inputStream = DataInputStream(shiftServer.accept().getInputStream())
        while (true) {
            val string = inputStream.readUTF()
            PerformActions.performShiftAction(Gson().fromJson(string, Boolean::class.java))
        }
    }
}