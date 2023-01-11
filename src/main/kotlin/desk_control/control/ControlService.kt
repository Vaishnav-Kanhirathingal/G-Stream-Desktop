package desk_control.control

import com.google.gson.Gson
import desk_control.data.JoyStickControls
import desk_control.data.MouseData
import desk_control.data.PadControls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.net.ServerSocket
import java.text.SimpleDateFormat

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
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch { initiateMovementServer() }
        scope.launch { initiateGamePadServer() }
        scope.launch { initiateMouseTrackServer() }
        scope.launch { initiateShiftServer() }
//        scope.launch {
//            while (true) {
//                Thread.sleep(1000)
//                val x = MouseInfo.getPointerInfo().location.x
//                val y = MouseInfo.getPointerInfo().location.y
//                println("mouse: x = $x\t\ty = $y")
//            }
//        }
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
            println("time - ${SimpleDateFormat("[yyyy-MM-dd]-[HH:mm:ss.SSS]").format(System.currentTimeMillis())}")
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