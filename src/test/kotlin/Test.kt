import java.net.NetworkInterface
import java.util.*

fun main(args: Array<String>) {
    repeat(5) {
        Thread.sleep(1000)
        println("some")
    }
}

fun displayInterfaceInformation(): String? {
    for (netInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
        val listOfAddresses = Collections.list(netInterface.inetAddresses)
        val isWlan = netInterface.name.contains("wlan")
        if (listOfAddresses.isNotEmpty() && isWlan) {
            for (inetAddress in listOfAddresses) {
                if (inetAddress.hostAddress.startsWith("192.168"))
                    return inetAddress.hostAddress
            }
        }
    }
    return null
}