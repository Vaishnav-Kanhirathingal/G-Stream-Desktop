package UI

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.google.gson.Gson
import connection.ConnectionData
import kotlinx.coroutines.delay
import services.control.ControlService
import services.control.PerformActions
import services.control.data.PadMapping
import services.stream.StreamService
import java.awt.Desktop
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.URI
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    application {
        Window(
            icon = getImageFromUrl("https://github.com/Vaishnav-Kanhirathingal/G-Stream-Desktop/blob/main/src/main/resources/app_icon_mipmap/mipmap-hdpi/ic_launcher.png?raw=true"),
            title = "G-Stream",
            onCloseRequest = ::exitApplication,
            resizable = true,
            content = { MaterialTheme { app() } },
            state = WindowState(height = 660.dp, width = 700.dp)
        )
    }
}

lateinit var controlService: ControlService
lateinit var streamService: StreamService

@Preview
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

    val gameList = remember { mutableStateListOf(PadMapping.deathStranding, PadMapping.control) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(ScrollState(0))
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            fpsCounter()
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Column {
                        Image(painter = getConnectionImagePainter(), contentDescription = null)
                        Text(text = "Scan this QRCode from the G-Stream\napp to initiate streaming from this device.")
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(start = 20.dp).fillMaxWidth(),
                        content = {
                            statusBox(active = leftGamePadServerRunning, serverName = "Left Game Pad Server")
                            statusBox(active = leftJoystickServerRunning, serverName = "Left Joystick Server")
                            statusBox(active = audioServerRunning, serverName = "Audio Server")
                            statusBox(active = videoServerRunning, serverName = "Video Server")
                            statusBox(active = rightJoystickServerRunning, serverName = "Right Joystick Server")
                            statusBox(active = rightGamePadServerRunning, serverName = "Right Game Pad Server")
                        }
                    )
                }
            )
            addGameTextField(
                addGameToList = {
                    try {
                        val x = Gson().fromJson(it, PadMapping::class.java)
                        gameList.add(x)
                        // TODO: display that the process has completed
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            )
            linksRow(modifier = Modifier.fillMaxWidth())
            gameOptionBox(gameList = gameList)
        }
    )
}

@Composable
fun fpsCounter() {
    val fps = remember { mutableStateOf(0) }
    LaunchedEffect(
        key1 = fps,
        block = {
            while (true) {
                delay(1000L)
                fps.value = streamService.getFrames()
            }
        }
    )
    Text(text = "FPS: ${fps.value}")
}

@Composable
fun statusBox(active: Boolean, serverName: String) {
    val statusSize = 12.dp
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = ColorPainter(if (active) Color.Green else Color.Red),
            contentDescription = null,
            modifier = Modifier.width(statusSize).height(statusSize)
        )
        Text(
            text = serverName,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
fun addGameTextField(addGameToList: (String) -> Unit) {
    var jsonText by remember { mutableStateOf("") }
    TextField(
        value = jsonText,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { jsonText = it; println(jsonText) },
        label = { Text("Enter a json formatted string here to add support for a new game. (read desktop documentation)") },
        trailingIcon = {
            IconButton(
                onClick = {
                    addGameToList(jsonText)
                    jsonText = ""
                },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null
                    )
                }
            )
        },
    )
}

@Composable
fun linksRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { openUrl("https://github.com/Vaishnav-Kanhirathingal/G-Stream-Desktop/blob/main/README.md") },
                content = { Text(text = "Desktop Documentation") }
            )
            Button(
                modifier = Modifier.weight(1f),
                onClick = { openUrl("https://github.com/Vaishnav-Kanhirathingal/G-Stream-MOBILE/blob/main/README.md") },
                content = { Text(text = "Android Documentation") }
            )
            Button(
                modifier = Modifier.weight(1f),
                onClick = { openUrl("https://github.com/Vaishnav-Kanhirathingal/G-Stream-MOBILE/releases") },
                content = { Text(text = "Android APK download") }
            )
        }
    )
}

@Preview
@Composable
fun gameOptionBox(gameList: List<PadMapping>) {
    var gameState by remember { mutableStateOf(PadMapping.deathStranding) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Text(text = "Select the game to play :-", fontSize = 20.sp)
            gameList.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        RadioButton(
                            selected = gameState == it,
                            onClick = {
                                PerformActions.game = it
                                gameState = it
                            }
                        )
                        Text(
                            text = it.gameName,
                            modifier = Modifier.padding(PaddingValues.Absolute(left = 10.dp)),
                            fontSize = 20.sp
                        )
                    }
                )
            }
        }
    )
}

fun getConnectionImagePainter(size: Int = 300): Painter {
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
                if (inetAddress.hostAddress.startsWith("192.168") && !netInterface.displayName.contains("Direct")) {
                    println(
                        "\n\nDisplay name:\t${netInterface.displayName}\n" +
                                "Name:\t\t\t${netInterface.name}\n" +
                                "InetAddress:\t${inetAddress.hostAddress}"
                    )
                    return inetAddress.hostAddress
                }
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

fun openUrl(url: String) {
    try {
        Desktop.getDesktop().browse(URI(url))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}