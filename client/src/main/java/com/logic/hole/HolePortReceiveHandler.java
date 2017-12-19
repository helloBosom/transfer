package com.logic.hole;

import com.google.gson.Gson;
import com.logic.message.Message;
import com.logic.message.SecondHandshakeMessage;
import com.logic.message.SendFileMessage;
import com.logic.message.ThirdHandshakeMessage;
import com.logic.utils.HandshakeField;
import com.logic.utils.InetUtil;

import java.io.*;
import java.net.Socket;

/**
 * 接收到第二次握手，予以回复第三次握手消息
 * 收到可以发送文件消息，开始发送文件
 */
public class HolePortReceiveHandler implements Runnable {
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    String fileLocation;

    public HolePortReceiveHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            inputStream = new DataInputStream(socket.getInputStream());

            outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int temp;
            while ((temp = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, temp);
            }
            String str = outputStream.toString();
            //TODO
            Message message = new Gson().fromJson(str, Message.class);
            //第二次握手
            if (message instanceof SecondHandshakeMessage) {
                SecondHandshakeMessage secondHandshakeMessage = (SecondHandshakeMessage) message;
                dealSecondHandshakeMessage(secondHandshakeMessage);
            }
            //可以发送文件
            if (message instanceof SendFileMessage) {
                SendFileMessage sendFileMessage = (SendFileMessage) message;
                dealSendFileMessage(sendFileMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void dealSecondHandshakeMessage(SecondHandshakeMessage secondHandshakeMessage) {
        if (HandshakeField.SECOND_HANDSHAKE.equals(secondHandshakeMessage.getHeader())) {
            ThirdHandshakeMessage thirdHandshakeMessage = new ThirdHandshakeMessage();
            thirdHandshakeMessage.setHeader(HandshakeField.THIRD_HANDSHAKE);
            try {
                thirdHandshakeMessage.setId(InetUtil.getProperties("").getProperty("localId"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO 发送第三次握手消息

        }
    }

    /**
     * 发送文件
     *
     * @param sendFileMessage
     */
    private void dealSendFileMessage(SendFileMessage sendFileMessage) {
        if (HandshakeField.SENDFILE.equals(sendFileMessage.getHeader())) {
            File download = new File(fileLocation);
            try {
                //TODO 发送文件
                RandomAccessFile raf = new RandomAccessFile(download, "rw");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
