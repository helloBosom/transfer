package fun.peri.handler;

import fun.peri.manager.ConnectManager;
import fun.peri.message.FileConnectMessage;
import fun.peri.service.Transfer;

import java.util.TimerTask;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 处理打洞申请队列，发送打洞请求，处理完将消息从队列中移除
 * @author hellobosom@gmail.com
 */
public class ApplyConnectHandler extends TimerTask {

    /**
     * 平时保留的核心线程数
     */
    private int corePoolSize = 300;
    /**
     * 线程池最大线程数
     */
    private int maximumPoolSize = 500;
    /**
     * 超出核心线程数量的线程保留时间
     */
    private long keepAliveTime = 300;
    /**
     * keepAliveTime单位
     */
    private TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
    private SynchronousQueue synchronousQueue = new SynchronousQueue();

//    ThreadFactory factory = new T

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, keepAliveTimeUnit, synchronousQueue);

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
