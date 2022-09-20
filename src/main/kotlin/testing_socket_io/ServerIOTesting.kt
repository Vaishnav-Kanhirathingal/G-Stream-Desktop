package testing_socket_io

import com.google.gson.Gson
import desk_control.Control
import desk_control.PerformActions
import java.io.DataInputStream
import java.net.ServerSocket

class ServerIOTesting {
    val serverSocket = ServerSocket(0)

    suspend fun initiateDataGetter() {
        val inputStream = DataInputStream(serverSocket.accept().getInputStream())
        for (i in 0..200) {
            val string = inputStream.readUTF()
            print("string received = $string")
            PerformActions.performAction(Gson().fromJson(string, Control::class.java))
        }
        closeSocket()
    }

    private suspend fun closeSocket() {
        print("closed port")
        serverSocket.close()
    }
}