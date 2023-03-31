package UI

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.gson.Gson
import connection.ConnectionData
import services.control.ControlService
import services.control.PerformActions
import services.control.data.PadMapping
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
        resizable = true,
    ) {
        MaterialTheme { app() }
    }
}

lateinit var controlService: ControlService
lateinit var streamService: StreamService

@Preview
@OptIn(ExperimentalUnitApi::class)
@Composable
fun app() {
    var leftJoystickServerRunning by remember { mutableStateOf(true) }
    var leftGamePadServerRunning by remember { mutableStateOf(true) }
    var rightGamePadServerRunning by remember { mutableStateOf(true) }
    var rightJoystickServerRunning by remember { mutableStateOf(true) }
    controlService = ControlService(
        leftJoystickServerError = { leftJoystickServerRunning = false },
        leftGamePadServerError = { leftGamePadServerRunning = false },
        rightGamePadServerError = { rightGamePadServerRunning = false },
        rightJoystickServerError = { rightJoystickServerRunning = false },
    )

    var audioServerRunning by remember { mutableStateOf(true) }
    var videoServerRunning by remember { mutableStateOf(true) }
    streamService = StreamService(audioServerError = { audioServerRunning = false },
        videoServerError = { videoServerRunning = false })
    val statusSize = 12.dp

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(ScrollState(0)).padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.fillMaxWidth().height(20.dp))
        Text(
            text = "Server Status", fontSize = TextUnit(
                value = 16f, type = TextUnitType.Sp
            )
        )
        Spacer(Modifier.fillMaxWidth().height(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = ColorPainter(if (leftGamePadServerRunning) Color.Green else Color.Red),
                    contentDescription = null,
                    modifier = Modifier.width(statusSize).height(statusSize)
                )
                Text(text = "Left Game Pad Server", modifier = Modifier.padding(start = 10.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = ColorPainter(if (leftJoystickServerRunning) Color.Green else Color.Red),
                    contentDescription = null,
                    modifier = Modifier.width(statusSize).height(statusSize)
                )
                Text(text = "Left Joystick Server", modifier = Modifier.padding(start = 10.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = ColorPainter(if (audioServerRunning) Color.Green else Color.Red),
                    contentDescription = null,
                    modifier = Modifier.width(statusSize).height(statusSize)
                )
                Text(text = "Audio Server", modifier = Modifier.padding(start = 10.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = ColorPainter(if (videoServerRunning) Color.Green else Color.Red),
                    contentDescription = null,
                    modifier = Modifier.width(statusSize).height(statusSize)
                )
                Text(text = "Video Server", modifier = Modifier.padding(start = 10.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = ColorPainter(if (rightJoystickServerRunning) Color.Green else Color.Red),
                    contentDescription = null,
                    modifier = Modifier.width(statusSize).height(statusSize)
                )
                Text(text = "Right Joystick Server", modifier = Modifier.padding(start = 10.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = ColorPainter(if (rightGamePadServerRunning) Color.Green else Color.Red),
                    contentDescription = null,
                    modifier = Modifier.width(statusSize).height(statusSize)
                )
                Text(text = "Right Game Pad Server", modifier = Modifier.padding(start = 10.dp))
            }
        }
        Spacer(Modifier.fillMaxWidth().height(20.dp))
        Image(painter = getConnectionImagePainter(), contentDescription = null)
        Text(text = "Scan this QRCode from the G-Stream app to initiate streaming from this device.")
        Spacer(Modifier.fillMaxWidth().height(20.dp))
        gameOptionBox()
    }
}


@Composable
fun gameOptionBox() {
    var gameState by remember { mutableStateOf(PadMapping.deathStranding) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Select the game to play", fontSize = 20.sp)
        PadMapping.getValue.forEach {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = gameState == it, onClick = {
                    PerformActions.game = it
                    gameState = it
                })
                Text(
                    text = it.gameName,
                    modifier = Modifier.padding(PaddingValues.Absolute(left = 10.dp)),
                    fontSize = 20.sp
                )
            }
        }
    }
}

fun getConnectionImagePainter(size: Int = 400): Painter {
    val data = Gson().toJson(
        ConnectionData(
            serverIpAddress = getAddress() ?: "null",
            videoPort = streamService.screenPort,
            audioPort = streamService.audioPort,
            leftGamePadPort = controlService.leftGamePadPort,
            leftJoyStickPort = controlService.leftJoystickPort,
            rightGamePadPort = controlService.rightGamePadPort,
            rightJoyStickPort = controlService.rightJoystickPort,
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
                if (inetAddress.hostAddress.startsWith("192.168")) return inetAddress.hostAddress
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
    return org.jetbrains.skia.Image.makeFromEncoded(stream.toByteArray()).toComposeImageBitmap().toAwtImage()
        .toPainter()
}