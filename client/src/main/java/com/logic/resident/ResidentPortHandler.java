package com.logic.resident;

import com.google.gson.Gson;
import com.logic.manager.ConnectManager;
import com.logic.message.*;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 常驻客户端处理类，接受服务端消息并处理
 */
public class ResidentPortHandler implements Runnable {
    Socket socket;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    public ResidentPortHandler(Socket socket) {
        this.socket = socket;
    }

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
                Message message = new Gson().fromJson(str, Message.class);
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
        natInfoMessage.setHeader("connect");
        natInfoMessage.setOutsideIp(connectedMessage.getRemoteOutsideIp());
        natInfoMessage.setOutsidePort(connectedMessage.getRemoteOutsidePort());
        natInfoMessage.setInsideIp(connectedMessage.getRemoteInsideIp());
        natInfoMessage.setInsidePort(connectedMessage.getRemoteInsidePort());
        natInfoMessage.setId(connectedMessage.getId());
        ConnectManager.connected.add(natInfoMessage);
    }
}

