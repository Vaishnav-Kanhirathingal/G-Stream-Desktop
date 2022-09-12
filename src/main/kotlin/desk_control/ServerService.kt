package desk_control

import com.google.gson.Gson

class ServerService {
    fun main() {
        displayQRCodeOnScreen()
        startControlService()
        startScreenSharingService()
    }

    private fun displayQRCodeOnScreen() {
        // TODO: show a qr code containing screen details on the screen.
    }

    private fun startControlService() {
        // TODO: get control data using http sockets.
    }

    private fun startScreenSharingService() {
        // TODO: send screen data using udp sockets
    }

    private fun performControls(controlJson: String) {
        try {
            PerformActions.performAction(Gson().fromJson(controlJson, Control::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}