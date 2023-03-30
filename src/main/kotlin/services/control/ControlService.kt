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
 * @param leftJoystickServerError executed after left joystick server error
 * @param leftGamePadServerError executed after left gamePad server error
 * @param rightJoystickServerError executed after right joystick server error
 * @param rightGamePadServerError executed after right gamePad server error
 */
class ControlService(
    private val leftJoystickServerError: () -> Unit,
    private val leftGamePadServerError: () -> Unit,
    private val rightJoystickServerError: () -> Unit,
    private val rightGamePadServerError: () -> Unit,
) {
    private val leftJoystickServer = ServerSocket(0)
    private val leftGamePadServer = ServerSocket(0)
    private val rightJoystickServer = ServerSocket(0)
    private val rightGamePadServer = ServerSocket(0)

    val leftJoystickPort get() = leftJoystickServer.localPort
    val leftGamePadPort get() = leftGamePadServer.localPort
    val rightJoystickPort get() = rightJoystickServer.localPort
    val rightGamePadPort get() = rightGamePadServer.localPort

    init {
        CoroutineScope(Dispatchers.IO).apply {
            launch { initiateLeftJoystickServer() }
            launch { initiateRightGamePadServer() }
            launch { initiateRightJoystickServer() }
            launch { initiateLeftGamePadServer() }
        }
    }

    private suspend fun initiateLeftJoystickServer() {
        val inputStream = DataInputStream(leftJoystickServer.accept().getInputStream())
        try {
            while (true) {
                val string = inputStream.readUTF()
                PerformActions.performMovementAction(Gson().fromJson(string, JoyStickControls::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace();leftJoystickServerError()
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

    private suspend fun initiateRightJoystickServer() {
        val inputStream = DataInputStream(rightJoystickServer.accept().getInputStream())
        try {
            while (true) {
                val string = inputStream.readUTF()
                PerformActions.performMouseTrackAction(Gson().fromJson(string, MouseData::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace();rightJoystickServerError()
        }
    }

    private suspend fun initiateRightGamePadServer() {
        val inputStream = DataInputStream(rightGamePadServer.accept().getInputStream())
        try {
            while (true) {
                val string = inputStream.readUTF()
                PerformActions.performRightGamePadAction(Gson().fromJson(string, PadControls::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace();rightGamePadServerError()
        }
    }
}