package com.logic.hole;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 */
public class HolePortReceive {
    Socket socket;

    public void start(InetAddress remoteAddress, int remotePort, InetAddress localAddress, int localPort) {
        try {
            socket = new Socket(remoteAddress, remotePort, localAddress, localPort);
            socket.setReuseAddress(true);
            new Thread(new HolePortReceiveHandler(socket)).start();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
