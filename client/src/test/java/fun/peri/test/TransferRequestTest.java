package fun.peri.test;

import fun.peri.handler.ApplyConnectHandler;
import fun.peri.handler.ConnectRemoteHandler;
import fun.peri.manager.Management;
import fun.peri.resident.ResidentPort;
import fun.peri.util.InetUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Timer;

public class TransferRequestTest {

    /**
     * 常驻客户端端口,连接常驻服务器端口
     * 中括号是数组类型的一部分
     */
    public static void main(String[] args) {

        Timer timer = new Timer();
        timer.schedule(new ApplyConnectHandler(),3,1000);

        ResidentPort residentPort = new ResidentPort();
        Management.manager.put("residentPort", residentPort);
        String profileLocation = "Inet.properties";
        try {
            String remoteIp = InetUtil.getProperties(profileLocation).getProperty("remoteIp");
            int remotePort = Integer.parseInt(InetUtil.getProperties(profileLocation).getProperty("remotePort"));
            String localIp = InetUtil.getProperties(profileLocation).getProperty("localIp");
            int localPort = Integer.parseInt(InetUtil.getProperties(profileLocation).getProperty("localPort"));
            residentPort.start(InetAddress.getByName(remoteIp), remotePort, InetAddress.getByName(localIp), localPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *处理打洞请求
     */
    static {
        new Thread(new ApplyConnectHandler()).start();
    }

    /**
     * 处理连接请求
     */
    static {
        new Thread(new ConnectRemoteHandler()).start();
    }

}

