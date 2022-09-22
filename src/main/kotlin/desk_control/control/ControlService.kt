package desk_control.control

import com.google.gson.Gson
import desk_control.data.Control
import java.io.DataInputStream
import java.net.ServerSocket

class ControlService {
    val serverSocket = ServerSocket(0)

    suspend fun initiateDataGetter() {
        val inputStream = DataInputStream(serverSocket.accept().getInputStream())
        while (true) {
            val string = inputStream.readUTF()
            println("string received = $string")
            PerformActions.performAction(Gson().fromJson(string, Control::class.java))
        }
    }

    private fun closeSocket() {
        print("closed port")
        serverSocket.close()
    }
}