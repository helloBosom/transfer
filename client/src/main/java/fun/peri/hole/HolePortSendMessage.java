package fun.peri.hole;

import fun.peri.message.BaseMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 发送消息
 *
 * @author goon
 */
public class HolePortSendMessage {

    private Socket socket;

    /**
     * @param remoteAddress 远端地址
     * @param remotePort    远端端口
     * @param localAddress  本地地址
     * @param localPort     本地端口
     */
    public void start(BaseMessage message, InetAddress remoteAddress, int remotePort, InetAddress localAddress, int localPort) {
        try {
            socket = new Socket(remoteAddress, remotePort, localAddress, localPort);
            socket.setReuseAddress(true);
            new Thread(new HolePortSendMessageHandler(socket, message)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
