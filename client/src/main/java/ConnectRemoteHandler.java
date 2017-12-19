import com.logic.hole.HolePortSendMessage;
import com.logic.manager.ConnectManager;
import com.logic.message.ConnectMessage;
import com.logic.message.FirstHandshakeMessage;
import com.logic.utils.HandshakeField;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 连接消息处理类
 */
public class ConnectRemoteHandler implements Runnable {
    @Override
    public void run() {
        while (true) {
            ConnectMessage connectMessage = ConnectManager.connect.getFirst();
            FirstHandshakeMessage firstHandshakeMessage = new FirstHandshakeMessage();
            firstHandshakeMessage.setHeader(HandshakeField.FIRST_HANDSHAKE);
            try {
                new HolePortSendMessage().start(firstHandshakeMessage, InetAddress.getByName(connectMessage.getRemoteIp()), connectMessage.getRemotePort(), InetAddress.getByName(connectMessage.getLocalIp()), connectMessage.getLocalPort());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}
