package fun.peri.handler;

import fun.peri.manager.ConnectManager;
import fun.peri.message.FileConnectMessage;
import fun.peri.service.Transfer;

/**
 * 处理打洞申请队列，发送打洞请求，处理完将消息从队列中移除
 */
public class ApplyConnectHandler implements Runnable {

    @Override
    public void run() {
        while (true) {
            FileConnectMessage fileConnectMessage = ConnectManager.fileConnect.getFirst();
            Transfer transfer = new Transfer();
            //打洞请求
            transfer.start(fileConnectMessage.getId());
            ConnectManager.processedTransfer.put(fileConnectMessage.getId(), fileConnectMessage.getFileLocation());
            ConnectManager.fileConnect.removeFirst();
        }
    }

}
