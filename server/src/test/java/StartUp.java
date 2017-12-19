import java.net.InetAddress;
import java.net.UnknownHostException;

public class StartUp {
    public static void main(String[] args) throws UnknownHostException {
        Server server = new Server();
        server.start(InetAddress.getByName(InetUtil.getLocalIp()), 1000, 8612);
        ProxyServer proxyServer = new ProxyServer();
        proxyServer.start(InetAddress.getByName(InetUtil.getLocalIp()), 1000, 8613);
    }
}
