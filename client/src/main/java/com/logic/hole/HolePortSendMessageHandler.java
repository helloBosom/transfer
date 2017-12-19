package com.logic.hole;

import com.logic.message.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HolePortSendMessageHandler implements Runnable {

    Socket socket;
    DataOutputStream dataOutputStream;
    Message message;

    public HolePortSendMessageHandler(Socket socket, Message message) {
        this.socket = socket;
        this.message = message;
    }

    public void run() {
        if (null != message) {
            try {
                //TODO
                dataOutputStream.writeUTF(message.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
