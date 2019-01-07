package fun.peri.handler;

import fun.peri.constant.Constants;
import fun.peri.hole.HolePortSendMessage;
import fun.peri.manager.ConnectManager;
import fun.peri.message.ConnectMessage;
import fun.peri.message.FirstHandshakeMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 连接消息处理类
 */
public class ConnectRemoteHandler implements Runnable {

    public void run() {
        while (true) {
            ConnectMessage connectMessage = ConnectManager.connect.getFirst();
            FirstHandshakeMessage firstHandshakeMessage = new FirstHandshakeMessage();
            firstHandshakeMessage.setHeader(Constants.FIRST_HANDSHAKE);
            try {
                new HolePortSendMessage().start(firstHandshakeMessage, InetAddress.getByName(connectMessage.getRemoteIp()), connectMessage.getRemotePort(), InetAddress.getByName(connectMessage.getLocalIp()), connectMessage.getLocalPort());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

}
