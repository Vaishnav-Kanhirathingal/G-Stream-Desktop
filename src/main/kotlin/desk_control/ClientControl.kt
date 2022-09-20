package desk_control

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.net.Socket

class ClientControl {
    fun main(ip: String = "localhost", port: Int) {
        // TODO: 19-08-2022 connect with the server and send dummy messages to the server to check controls.
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(5000)
            try {
                val control = Control(
                    mouseData = MouseData(0, 0),
                    gamePad = PadControls.CIRCLE,
                    playerMovement = JoyStickControls.STICK_LEFT,
                    shift = false
                )
                val socket = Socket(ip, port)
                val d = DataOutputStream(socket.getOutputStream())
                d.writeUTF(Gson().toJson(control))
                d.flush()
                d.close()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}