package fun.peri.hole;

import fun.peri.message.BaseMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author goon
 */
public class HolePortSendMessageHandler implements Runnable {

    Socket socket;
    DataOutputStream dataOutputStream;
    BaseMessage message;

    public HolePortSendMessageHandler(Socket socket, BaseMessage message) {
        this.socket = socket;
        this.message = message;
    }

    @Override
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
