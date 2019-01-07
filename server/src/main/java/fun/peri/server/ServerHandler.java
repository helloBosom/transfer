package fun.peri.server;

import com.google.gson.Gson;
import fun.peri.message.Message;

import java.io.*;
import java.net.Socket;

public class ServerHandler implements Runnable {

    private Socket socket;
    InputStream in;
    OutputStream outputStream;

    public ServerHandler(Socket socket) {
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
            //TODO
            Message message = new Gson().fromJson(str, Message.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
