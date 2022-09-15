package testing_socket_io

import java.net.ServerSocket

class ServerIOTesting {
}

fun main(args: Array<String>) {
    val serverSocket = ServerSocket(0)
    print("port value = ${serverSocket.localPort}")//works as expected. returns an empty port number.
    serverSocket.accept()
    serverSocket.close()
}