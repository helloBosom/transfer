import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    List<Socket> connNumber = new ArrayList<Socket>();
    Socket socket;
    int count = 0;
    static final Logger logger = LoggerFactory.getLogger(Server.class);

    /**
     * @param inetAddress 服务端绑定的ip地址
     * @param backlog     最大连接数
     * @param port        端口号
     */
    public void start(InetAddress inetAddress, int backlog, int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port, backlog, inetAddress);
            serverSocket.setReuseAddress(true);
            while (true) {
                socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = new ByteArrayOutputStream();
                int temp;
                byte[] bytes = new byte[64];
                while ((temp = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, temp);
                }
                String id = outputStream.toString();
                ConnectManager.sockets.put(id, socket);
                new Thread(new ServerHandler(socket)).start();
                connNumber.add(socket);
                count++;
                logger.info("Server number:" + count);
                /**
                 *
                 */
                NATInfo natInfo = ConnectManager.awaitConnect.getFirst();
                ConnectMessage connectMessage = new ConnectMessage();
                connectMessage.setRemoteIp(natInfo.getIp());
                connectMessage.setHeader(Header.Connect);
                connectMessage.setLocalPort(natInfo.getPort());
                //TODO message转byte[]
                socket = ConnectManager.sockets.get(natInfo.getId());
                try {
                    outputStream = socket.getOutputStream();
                    //TODO outputStream.write();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ConnectManager.awaitConnect.removeFirst();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
