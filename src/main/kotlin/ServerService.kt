package main

class ServerService {
    fun main() {
        displayQRCodeOnScreen()
        startControlService()
        startScreenSharingService()
    }

    fun displayQRCodeOnScreen() {
        // TODO: show a qr code containing screen details on the screen.
    }

    fun startControlService() {
        // TODO: get control data using http sockets.
    }

    fun startScreenSharingService() {
        // TODO: send screen data using udp sockets
    }

    fun performControls(control: Control) {
        // TODO: perform controls
    }
}