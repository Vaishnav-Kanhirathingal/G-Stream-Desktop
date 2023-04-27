import java.net.NetworkInterface
import java.util.*

fun main() {
    getAddress()
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
