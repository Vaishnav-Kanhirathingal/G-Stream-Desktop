package UI

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.gson.Gson
import connection.ConnectionData
import services.control.ControlService
import services.stream.StreamService
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

fun main() = application {
    Window(
        icon = getImageFromUrl("https://github.com/Vaishnav-Kanhirathingal/G-Stream-Desktop/blob/main/src/main/resources/app_icon_mipmap/mipmap-hdpi/ic_launcher.png?raw=true"),
        title = "G-Stream",
        onCloseRequest = ::exitApplication,
        resizable = true
    ) {
        MaterialTheme { app() }
    }
}

lateinit var controlService: ControlService
lateinit var streamService: StreamService

@Composable
fun app() {
    var movementServerRunning by remember { mutableStateOf(true) }
    var gamePadServerRunning by remember { mutableStateOf(true) }
    var mouseTrackServerRunning by remember { mutableStateOf(true) }
    var leftGamePadServerRunning by remember { mutableStateOf(true) }
    controlService = ControlService(
        movementServerError = { movementServerRunning = false },
        gamePadServerError = { gamePadServerRunning = false },
        mouseTrackServerError = { mouseTrackServerRunning = false },
        leftGamePadServerError = { leftGamePadServerRunning = false },
    )

    var audioServerRunning by remember { mutableStateOf(true) }
    var videoServerRunning by remember { mutableStateOf(true) }
    streamService = StreamService(
        audioServerError = { audioServerRunning = false },
        videoServerError = { videoServerRunning = false }
    )

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(ScrollState(0)).horizontalScroll(ScrollState(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text("movement server running = $movementServerRunning")
            Text("gamePad server running = $gamePadServerRunning")
            Text("mouseTrack server running = $mouseTrackServerRunning")
            Text("leftGamePad server running = $leftGamePadServerRunning")
            Text("audio server running = $audioServerRunning")
            Text("video server running = $videoServerRunning")
        }
        Image(
            painter = getConnectionImagePainter(),
            contentDescription = null,
        )
        Text(text = "Scan this QRCode from the G-Stream app to initiate streaming from this device.")
        Text(
            text = "Instructions:\ngg" +
                    "\n* Make sure Both the devices (This PC and the Android device) are on the same wifi network." +
                    "\n* Preferably use a WIFI-6 connection for seamless experience." +
                    "\n* A reduced load on the router ensures a better connection.",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


fun getConnectionImagePainter(size: Int = 400): Painter {
    val data = Gson().toJson(
        ConnectionData(
            serverIpAddress = getAddress() ?: "null",
            videoPort = streamService.screenPort,
            audioPort = streamService.audioPort,
            leftGamePadPort = controlService.leftGamePadPort,
            leftJoyStickPort = controlService.movementPort,
            rightGamePadPort = controlService.gamePadPort,
            rightJoyStickPort = controlService.mouseTrackPort,
        )
    )
    val link = "https://api.qrserver.com/v1/create-qr-code/?size=${size}x${size}&data=$data"
    return getImageFromUrl(link = link)
}

fun getAddress(): String? {
    for (netInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
        val listOfAddresses = Collections.list(netInterface.inetAddresses)
        val isWlan = netInterface.name.contains("wlan")
        if (listOfAddresses.isNotEmpty() && isWlan) {
            for (inetAddress in listOfAddresses) {
                println("\n\nDisplay name:\t${netInterface.displayName}\nName:\t\t\t${netInterface.name}\nInetAddress:\t${inetAddress.hostAddress}")
                if (inetAddress.hostAddress.startsWith("192.168"))
                    return inetAddress.hostAddress
            }
        }
    }
    return null
}

fun getImageFromUrl(link: String): Painter {
    val connection = URL(link).openConnection() as HttpURLConnection
    connection.connect()
    val stream = ByteArrayOutputStream()
    ImageIO.write(ImageIO.read(connection.inputStream), "png", stream)
    return org.jetbrains.skia.Image
        .makeFromEncoded(stream.toByteArray())
        .toComposeImageBitmap()
        .toAwtImage()
        .toPainter()
}