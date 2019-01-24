package fun.peri.handler;

import fun.peri.constant.TCPStatusEnum;
import fun.peri.hole.HolePortSendMessage;
import fun.peri.manager.ConnectManager;
import fun.peri.message.ConnectMessage;
import fun.peri.message.FirstHandshakeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 连接消息处理类
 * @author hellobosom@gmail.com
 */
public class ConnectRemoteHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ApplyConnectHandler.class);

    @Override
    public void run() {
        while (true) {
            ConnectMessage connectMessage = ConnectManager.connect.getFirst();
            FirstHandshakeMessage firstHandshakeMessage = new FirstHandshakeMessage();
            firstHandshakeMessage.setHeader(TCPStatusEnum.FIRST_HANDSHAKE);
            try {
                new HolePortSendMessage().start(firstHandshakeMessage, InetAddress.getByName(connectMessage.getRemoteIp()), connectMessage.getRemotePort(), InetAddress.getByName(connectMessage.getLocalIp()), connectMessage.getLocalPort());
            } catch (UnknownHostException e) {
                logger.error(e.getMessage());
            }
        }
    }

}
