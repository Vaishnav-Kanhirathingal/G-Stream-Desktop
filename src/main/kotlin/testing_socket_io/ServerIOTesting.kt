package testing_socket_io

import java.io.DataInputStream
import java.net.ServerSocket

class ServerIOTesting {
    val serverSocket = ServerSocket(0)

    suspend fun initiateDataGetter() {
        val inputStream = DataInputStream(serverSocket.accept().getInputStream())
        val string = inputStream.readUTF()
        print("string received = $string")
        closeSocket()
    }

    private suspend fun closeSocket() {
        print("closed port")
        serverSocket.close()
    }
}