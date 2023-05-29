import com.google.gson.GsonBuilder
import services.control.data.PadMapping
import java.awt.event.KeyEvent
import java.net.NetworkInterface
import java.time.LocalDate
import java.util.*

fun main() {
    getNewGameData()
}


fun getAddress() {
    for (netInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
        val listOfAddresses = Collections.list(netInterface.inetAddresses)

        val isWlan = netInterface.name.contains("wlan")
        if (listOfAddresses.isNotEmpty() && isWlan) {
            for (inetAddress in listOfAddresses) {
                if (inetAddress.hostAddress.startsWith("192.168") && !netInterface.displayName.contains("Direct")) {
                    println(
                        "\n\nDisplay name:\t${netInterface.displayName}\n" +
                                "Name:\t\t\t${netInterface.name}\n" +
                                "InetAddress:\t${inetAddress.hostAddress}"
                    )
                }
            }
        }
    }
}


fun getNewGameData() {
    println(
        GsonBuilder().setPrettyPrinting().create().toJson(
            PadMapping(
                leftPadBottom = KeyEvent.VK_C,
                leftPadRight = KeyEvent.VK_TAB,

                rightPadTop = KeyEvent.VK_F,
                rightPadBottom = KeyEvent.VK_CONTROL,
                rightPadLeft = KeyEvent.VK_Q,
                rightPadRight = KeyEvent.VK_E,
                gameName = "Control example"
            )
        )
    )
}


fun testDate() {
    val x: LocalDate = LocalDate.of(1, 11, 1)
    println(x.month.name)
}
