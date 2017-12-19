package com.logic.manager;

import com.logic.message.ConnectMessage;
import com.logic.message.FileConnectMessage;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 连接管理
 */
public class ConnectManager {
    public static LinkedList connected = new LinkedList();
    public static LinkedList<ConnectMessage> connect = new LinkedList();
    public static LinkedList<FileConnectMessage> fileConnect = new LinkedList();
    public static HashMap processedTransfer = new HashMap();
}
