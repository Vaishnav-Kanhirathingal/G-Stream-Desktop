// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    text = "Hello, Desktop!"
                },
                content = {
                    Text(text)
                }
            )

            Image(
                painter = loadNetworkImage().toAwtImage().toPainter(),
                contentDescription = "Scan this QRCode to connect to this game server to play games available on this device"
            )
        }
    }
}


fun loadNetworkImage(): ImageBitmap {
    val size = 300
    // TODO: data should be appropriate type
    val data = "someData"
    val url = URL("https://api.qrserver.com/v1/create-qr-code/?size=${size}x${size}&data=$data")
    val connection = url.openConnection() as HttpURLConnection
    connection.connect()

    val inputStream = connection.inputStream
    val bufferedImage = ImageIO.read(inputStream)

    val stream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, "png", stream)
    val byteArray = stream.toByteArray()
    return org.jetbrains.skia.Image.makeFromEncoded(byteArray).asImageBitmap()
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
    ) {
        App()
    }
}