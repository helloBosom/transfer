package fun.peri.test;

import fun.peri.proxy.ProxyServer;
import fun.peri.server.Server;
import fun.peri.util.InetUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class StartUp {

    public static void main(String[] args) throws UnknownHostException {
        Server server = new Server();
        server.start(InetAddress.getByName(InetUtil.getLocalIp()), 1000, 8612);
        ProxyServer proxyServer = new ProxyServer();
        proxyServer.start(InetAddress.getByName(InetUtil.getLocalIp()), 1000, 8613);
    }

}
