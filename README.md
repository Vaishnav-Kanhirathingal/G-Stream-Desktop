# G-Stream-Desktop

Desktop client of the game streaming service project.

## What is cloud gaming?

Cloud gaming is a service which gives its user the ability to play high-end games on any device using a server on the internet that does all the necessary computing for the game. The idea is to have the game run on a remote machine and have the user interact with this machine through an app to play the game. The server and device then communicate on what to be shared between them. The data usually contains what controller buttons the user pressed, where the joystick is, the frame to be displayed to give visual on the gaming device.

## What does this project aims at?

This project is an attempt to localize cloud gaming services. The way this project achieves this is by running high-end games on the user’s personal computer preferably a windows machine and streaming that game to their mobile device. The mobile app has joystick and game-pad buttons which are mapped to key-presses and mouse movement. The server receives this data and interprets it to perform necessary actions. To make sure the user can see the gameplay, The server would also stream the display to the mobile device. The localized implementation would run on a local network. This means that internet isn’t necessary to run the service. The network hops would take place locally between the mobile device and the PC through a LAN connection. This means the connection would be established using LAN IP addresses. The application uses a QR code to establish connection between the server (preferably a Windows machine) and the client (mobile device). The QR code containes the IP address of the server and the port numbers for different services such as port for gameplay screen sharing, joystick controls, etc. After establishing a connection, the user can launch the game of their choice and play as required. The strength and stability of the connection depends on the Wi-Fi connection strength and whether the network is already saturated with load. A free network would provide a lower latency and more stable connection.

## Project Repos

[![Game - Stream android app icon](https://github.com/Vaishnav-Kanhirathingal/G-Stream-MOBILE/blob/main/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png?raw=true "[Game - Stream Mobile] - This app is responsible for sending control signals to the desktop side. It also displays gameplay streamed from the PC")](https://github.com/Vaishnav-Kanhirathingal/G-Stream-MOBILE)
[![Game - Stream desktop app icon](https://github.com/Vaishnav-Kanhirathingal/G-Stream-Desktop/blob/main/src/main/resources/app_icon_mipmap/mipmap-xxxhdpi/ic_launcher.png?raw=true "[Game - Stream Desktop] - This app is responsible for recieving control signals from the android side. It also streams gameplay to the android device")](https://github.com/Vaishnav-Kanhirathingal/G-Stream-Desktop)

## Guide

### Start Screen

the start screen contains a QR code and 6 status lights which indicate the status of multiple connections created between the two devices. On the same screen We also have a text input box. Here, we can enter a JSON formatted text containing the data required to assign values for key bindings for a new game.

[todo - PIC of default screen]

For example:-

> You can get the required values for each button from [here](https://learn.microsoft.com/en-us/windows/win32/inputdev/virtual-key-codes). Before assgining, You need to convert each value to it's integer value. eg - `0x10 = 16`

With options:

``` JSON
{
  "leftPadTop": 32,
  "leftPadBottom": 67,
  "leftPadCenter": 16,
  "leftPadLeft": 4096,
  "leftPadRight": 9,
  "rightPadTop": 70,
  "rightPadBottom": 17,
  "rightPadCenter": 1024,
  "rightPadLeft": 81,
  "rightPadRight": 69,
  "gameName": "Game-Name"
}
```

Without options:

``` JSON
{
  "leftPadBottom": 67,
  "leftPadRight": 9,
  "rightPadTop": 70,
  "rightPadBottom": 17,
  "rightPadLeft": 81,
  "rightPadRight": 69,
  "gameName": "Game-Name"
}
```

NOTE:

- `leftPadTop`: optional if required to change
- `leftPadCenter`: optional if required to change
- `leftPadLeft`: optional if required to change & is used to perform mouse clicks, preferrably do not change
- `rightPadCenter`: optional if required to change & is used to perform mouse clicks, preferrably do not change

The default values for the `PadMapping` class include:

``` Kotlin
data class PadMapping(
    val leftPadTop: Int = VK_SPACE,
    val leftPadBottom: Int,
    val leftPadCenter: Int = VK_SHIFT,
    val leftPadLeft: Int = BUTTON3_DOWN_MASK,
    val leftPadRight: Int,

    val rightPadTop: Int,
    val rightPadBottom: Int,
    val rightPadCenter: Int = BUTTON1_DOWN_MASK,
    val rightPadLeft: Int,
    val rightPadRight: Int,

    val gameName: String
)
```

Every attribute without a default value is to be mentioned inside the text box. To continue using the default value for your new game, You simply ignore the attribute.

Example:

[todo - PIC with default value]
[todo - PIC without default value]

### Button Rows

- Desktop Documentation: Opens the readme documentation for the desktop application
- Android Documentation: Opens the readme documentation for the android application
- Android APK download: Opens the release section for the android application

### Game Selector

We can select the game we want from the radio group. The new game pad mapping gets added to the bottom of the list. we can then select the new game to use the new mappings.

## Gameplay Screenshots

[todo - PIC Desktop GIF]
[todo - PIC Android GIF]
