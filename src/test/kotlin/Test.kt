import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

fun main(args: Array<String>) {
    val list = NetworkInterface.networkInterfaces()
    displayInterfaceInformation()
}

fun displayInterfaceInformation() {
    for (netint in Collections.list(NetworkInterface.getNetworkInterfaces())) {
        println("Display name:\t${netint.displayName}")
        println("Name:\t${netint.name}")

        val inetAddresses: Enumeration<InetAddress> = netint.inetAddresses
        for (inetAddress in Collections.list(inetAddresses)) {
            println("InetAddress:\t${inetAddress.hostAddress}")
        }
        println("\n")
    }
}