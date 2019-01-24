package fun.peri.resident;

import com.google.gson.Gson;
import fun.peri.constant.TCPStatusEnum;
import fun.peri.manager.ConnectManager;
import fun.peri.message.*;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import java.util.Objects;

/**
 * 常驻客户端处理类，接受服务端消息并处理
 *
 * @author hellobosom@gmail.com
 */
public class ResidentPortHandler implements Runnable {

    private Socket socket;
    DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;


    public ResidentPortHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            if (null != socket.getInputStream()) {
                dataInputStream = new DataInputStream(socket.getInputStream());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];
                int temp;
                while ((temp = dataInputStream.read(bytes)) != -1) {
                    byteArrayOutputStream.write(bytes, 0, temp);
                }
                String str = byteArrayOutputStream.toString();
                //TODO
                BaseMessage message = new Gson().fromJson(str, BaseMessage.class);
                //打洞下载文件请求
                if (message instanceof FileConnectMessage) {
                    FileConnectMessage fileConnectMessage = (FileConnectMessage) message;
                    ConnectManager.fileConnect.add(fileConnectMessage);
                }
                //链接命令
                if (message instanceof ConnectMessage) {
                    ConnectMessage connectMessage = (ConnectMessage) message;
                    ConnectManager.connect.add(connectMessage);
                }
                //已链接命令
                if (message instanceof ConnectedMessage) {
                    ConnectedMessage connectedMessage = (ConnectedMessage) message;
                    if ("connected".equals(connectedMessage.getHeader())) {
                        dealConnectedMessage(connectedMessage);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dealConnectedMessage(ConnectedMessage connectedMessage) {
        NATInfoMessage natInfoMessage = new NATInfoMessage();
        natInfoMessage.setHeader(TCPStatusEnum.CONNECT);
        natInfoMessage.setOutsideIp(connectedMessage.getRemoteOutsideIp());
        natInfoMessage.setOutsidePort(connectedMessage.getRemoteOutsidePort());
        natInfoMessage.setInsideIp(connectedMessage.getRemoteInsideIp());
        natInfoMessage.setInsidePort(connectedMessage.getRemoteInsidePort());
        natInfoMessage.setId(connectedMessage.getId());
        ConnectManager.connected.add(natInfoMessage);
    }

}

