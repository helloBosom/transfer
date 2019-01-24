package fun.peri.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * @author hellobosom@gmail.com
 */
public class ProxyServer {

    private Socket socket;

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
