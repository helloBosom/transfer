import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

public class InetUtil {

    static String getLocalIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * @return
     * @since 1.6+
     */
    static String getLocalMac() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
            StringBuffer s = new StringBuffer();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    s.append("-");
                }
                String str = Integer.toHexString(mac[i] & 0xFF);
                s.append(str.length() == 1 ? "0" + str : str);
            }
            return s.toString().toUpperCase();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Properties properties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(""));
        return properties;
    }

    static InetProperties inetProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(""));
        InetProperties inetProperties = new InetProperties();
        inetProperties.setLocalIp(properties.getProperty("localIp"));
        inetProperties.setLocalPort(Integer.parseInt(properties.getProperty("localPort")));
        inetProperties.setServerIp(properties.getProperty("serverIp"));
        inetProperties.setServerPort(Integer.parseInt(properties.getProperty("serverPort")));
        inetProperties.setRemoteIp(properties.getProperty("remoteIp"));
        inetProperties.setRemotePort(Integer.parseInt(properties.getProperty("remotePort")));
        return inetProperties;
    }

}
