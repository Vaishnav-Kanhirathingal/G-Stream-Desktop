public class ServerStarter {
    public static void main(String[] args) {
        displayQRCodeOnScreen();
        startControlService();
        startScreenSharingService();
    }

    public static void displayQRCodeOnScreen() {
        // TODO: 19-08-2022 show a qr code containing screen details on the screen.
    }

    public static void startControlService() {
        // TODO: 19-08-2022 get control data using http sockets.
    }

    public static void startScreenSharingService() {
        // TODO: 19-08-2022 send screen data using udp sockets
    }
}
