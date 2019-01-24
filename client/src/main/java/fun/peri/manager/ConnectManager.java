package fun.peri.manager;

import fun.peri.message.ConnectMessage;
import fun.peri.message.FileConnectMessage;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 连接管理
 *
 * @author hellobosom@gmail.com
 */
public class ConnectManager {
    public static LinkedList connected = new LinkedList();
    public static LinkedList<ConnectMessage> connect = new LinkedList();
    public static LinkedList<FileConnectMessage> fileConnect = new LinkedList();
    public static HashMap processedTransfer = new HashMap();
}
