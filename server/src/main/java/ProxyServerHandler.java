import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ProxyServerHandler implements Runnable {

    private Socket socket;
    InputStream in;

    public ProxyServerHandler(Socket socket) {
        this.socket = socket;
    }

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
            Message message = new Gson().fromJson(str, Message.class);
            //协助打洞请求
            if (message instanceof TransferMessage) {
                TransferMessage transferMessage = (TransferMessage) message;
                if ("applyTransfer".equals(transferMessage.getHeader())) {
                    NATInfo natInfo = new NATInfo();
                    natInfo.setHeader(Header.Connect);
                    natInfo.setIp(socket.getInetAddress().getHostAddress());
                    natInfo.setPort(socket.getPort());
                    ConnectManager.awaitConnect.add(natInfo);
                }
            }
            //
            if (message instanceof ConnectedMessage) {
                ConnectedMessage connectedMessage = (ConnectedMessage) message;
                if ("connected".equals(connectedMessage.getHeader())) {
                    NATInfo natInfo = new NATInfo();
                    natInfo.setHeader(Header.Connect);
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
