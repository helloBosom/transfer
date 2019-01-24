package fun.peri.server;

import com.google.gson.Gson;
import fun.peri.message.BaseMessage;

import java.io.*;
import java.net.Socket;

/**
 * @author hellobosom@gmail.com
 */
public class ServerHandler implements Runnable {

    private Socket socket;
    InputStream in;
    OutputStream outputStream;

    public ServerHandler(Socket socket) {
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
            //TODO
            BaseMessage message = new Gson().fromJson(str, BaseMessage.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
