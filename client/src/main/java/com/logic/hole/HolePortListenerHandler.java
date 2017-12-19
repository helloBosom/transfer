package com.logic.hole;

import com.google.gson.Gson;
import com.logic.message.*;
import com.logic.utils.HandshakeField;
import com.logic.utils.InetUtil;

import java.io.*;
import java.net.Socket;

/**
 * 转为监听器后实现类，若收到远端第一次握手请求，则响应予以回复第二次握手
 * 若收到远端第三次握手请求，则响应予以回复可以开始传输文件
 */
public class HolePortListenerHandler implements Runnable {

    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    public HolePortListenerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            byte[] bytes = new byte[1024];
            int temp;
            outputStream = new ByteArrayOutputStream();
            while ((temp = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, temp);
            }
            String str = outputStream.toString();
            //TODO
            Message message = new Gson().fromJson(str, Message.class);
            //收到远端第一次握手消息
            if (message instanceof FirstHandshakeMessage) {
                FirstHandshakeMessage firstHandshakeMessage = (FirstHandshakeMessage) message;
                dealFirstHandshakeMessage(firstHandshakeMessage);
            }
            //收到远端第三次握手消息
            if (message instanceof ThirdHandshakeMessage) {
                ThirdHandshakeMessage thirdHandshakeMessage = (ThirdHandshakeMessage) message;
                dealThirdHandshakeMessage(thirdHandshakeMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dealFirstHandshakeMessage(FirstHandshakeMessage firstHandshakeMessage) {
        if (HandshakeField.FIRST_HANDSHAKE.equals(firstHandshakeMessage.getHeader())) {
            //向远端发送第二次握手消息
            SecondHandshakeMessage secondHandshakeMessage = new SecondHandshakeMessage();
            secondHandshakeMessage.setHeader(HandshakeField.SECOND_HANDSHAKE);
            try {
                secondHandshakeMessage.setId(InetUtil.getProperties("").getProperty("localId"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO 发送第二次握手消息
        }
    }

    private void dealThirdHandshakeMessage(ThirdHandshakeMessage thirdHandshakeMessage) {
        if (HandshakeField.THIRD_HANDSHAKE.equals(thirdHandshakeMessage.getHeader())) {
            SendFileMessage sendFileMessage = new SendFileMessage();
            sendFileMessage.setHeader(HandshakeField.SENDFILE);
            try {
                sendFileMessage.setId(InetUtil.getProperties("").getProperty("localId"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO 向远端发送可以开始发送文件消息
        }
    }
}
