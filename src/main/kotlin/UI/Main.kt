// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
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
import com.sun.net.httpserver.HttpServer
import connection.ConnectionData
import java.awt.Dimension
import java.awt.Toolkit
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.Inet4Address
import java.net.URL
import javax.imageio.ImageIO

fun main() = application {
    // TODO: get window icon url from mobile client
    Window(
        icon = getImageFromUrl("https://github.com/Vaishnav-Kanhirathingal/G-Stream-MOBILE/blob/main/app/src/main/res/mipmap-hdpi/ic_launcher.png?raw=true"),
        title = "G-Stream",
        onCloseRequest = ::exitApplication,
        resizable = false
    ) { App() }
}

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(ScrollState(0))
                .horizontalScroll(ScrollState(0)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = getConnectionImagePainter(),
                contentDescription = "Scan this QRCode to connect to this game server to play games available on this device",
            )
            Text(text = "Scan this QRCode from the G-Stream app to initiate streaming from this device.")
            Button(onClick = { text = "Hello, Desktop!" }, content = { Text(text) })
        }
    }
}

fun getConnectionImagePainter(size: Int = 400): Painter {
    // TODO: check frame rate cap
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    val data = Gson().toJson(
        ConnectionData(
            serverIpAddress = Inet4Address.getLocalHost().hostAddress,
            controlPort = getControlPort(),
            streamPort = getStreamingPort(),
            horizontalResolution = screenSize.getWidth().toInt(),
            verticalResolution = screenSize.getHeight().toInt(),
            frameRateCap = 60,
        )
    )
    val link = "https://api.qrserver.com/v1/create-qr-code/?size=${size}x${size}&data=$data"
    return getImageFromUrl(link = link)
}

fun getImageFromUrl(link: String): Painter {
    val connection = URL(link).openConnection() as HttpURLConnection
    connection.connect()
    val stream = ByteArrayOutputStream()
    ImageIO.write(ImageIO.read(connection.inputStream), "png", stream)
    return org.jetbrains.skia.Image.makeFromEncoded(stream.toByteArray())
        .toComposeImageBitmap()
        .toAwtImage()
        .toPainter()
}

/**
 * initiates a server at an unused port and returns that port number. This function initiates a streaming port.
 */
private fun getStreamingPort(): Int {
    // TODO: open up a port and send back the details
    return 0
}

/**
 * initiates a server at an unused port and returns that port number. This function initiates a control port.
 */
private fun getControlPort(): Int {
    // TODO: open up a port and send back the details
    return 0
}