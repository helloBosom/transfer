package fun.peri.proxy;

import com.google.gson.Gson;
import fun.peri.entity.Header;
import fun.peri.manager.ConnectManager;
import fun.peri.message.ConnectedMessage;
import fun.peri.message.BaseMessage;
import fun.peri.message.NATInfo;
import fun.peri.message.TransferMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
/**
 * @author hellobosom@gmail.com
 */
public class ProxyServerHandler implements Runnable {

    private Socket socket;
    private InputStream in;

    public ProxyServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int temp;
            while ((temp = in.read(bytes)) != -1) {
                byteOS.write(bytes, 0, temp);
            }
            String str = byteOS.toString();
            //TODO 序列化有问题，须统一消息格式
            BaseMessage message = new Gson().fromJson(str, BaseMessage.class);
            //协助打洞请求
            if (message instanceof TransferMessage) {
                TransferMessage transferMessage = (TransferMessage) message;
                if (Header.APPLY_TRANSFER.equals(transferMessage.getHeader())) {
                    NATInfo natInfo = new NATInfo();
                    natInfo.setHeader(Header.CONNECT);
                    natInfo.setIp(socket.getInetAddress().getHostAddress());
                    natInfo.setPort(socket.getPort());
                    ConnectManager.awaitConnect.add(natInfo);
                }
            }
            //
            if (message instanceof ConnectedMessage) {
                ConnectedMessage connectedMessage = (ConnectedMessage) message;
                if (Header.CONNECTED.equals(connectedMessage.getHeader())) {
                    NATInfo natInfo = new NATInfo();
                    natInfo.setHeader(Header.CONNECT);
                    natInfo.setIp(socket.getInetAddress().getHostAddress());
                    natInfo.setPort(socket.getPort());
                    ConnectManager.connected.add(natInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
