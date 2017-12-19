import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer {
    Socket socket;

    public void start(InetAddress inetAddress, int backlog, int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port, backlog, inetAddress);
            serverSocket.setReuseAddress(true);
            while (true) {
                socket = serverSocket.accept();
                new Thread(new ProxyServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
