package fun.peri.resident;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 常驻客户端
 */
public class ResidentPort {

    Socket socket;

    /**
     * @param remoteAddress 远端地址
     * @param remotePort    远端端口
     * @param localAddress  本地地址
     * @param localPort     本地端口
     */
    public void start(InetAddress remoteAddress, int remotePort, InetAddress localAddress, int localPort) {
        try {
            socket = new Socket(remoteAddress, remotePort, localAddress, localPort);
            socket.setReuseAddress(true);
            new Thread(new ResidentPortHandler(socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}