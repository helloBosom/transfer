package fun.peri.hole;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 连接端口转为监听服务器，并放入管理类，此处不调用关闭
 *
 * @author hellobosom@gmail.com
 */
public class HolePortListener {

    private ServerSocket serverSocket;

    /**
     * @param inetAddress 服务端绑定的ip地址
     * @param backlog     最大连接数
     * @param port        端口号
     */
    public void start(InetAddress inetAddress, int backlog, int port) {
        try {
            serverSocket = new ServerSocket(port, backlog, inetAddress);
            Socket socket;
            while (true) {
                socket = serverSocket.accept();
                new Thread(new HolePortListenerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
