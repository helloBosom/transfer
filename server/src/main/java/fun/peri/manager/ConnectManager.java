package fun.peri.manager;

import fun.peri.message.NATInfo;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ConnectManager {
    public static LinkedList<NATInfo> awaitConnect = new LinkedList<NATInfo>();
    public static List<NATInfo> connected = new LinkedList<NATInfo>();
    public static HashMap<String, Socket> sockets = new HashMap<String, Socket>();
}