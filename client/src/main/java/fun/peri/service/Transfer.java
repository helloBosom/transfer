package fun.peri.service;

import fun.peri.constant.TCPStatusEnum;
import fun.peri.hole.HolePortListener;
import fun.peri.hole.HolePortSendMessage;
import fun.peri.manager.Management;
import fun.peri.message.TransferMessage;
import fun.peri.util.InetUtil;

import java.io.IOException;
import java.net.InetAddress;

/**
 * 实现类
 *
 * @author hellobosom@gmail.com
 */
public class Transfer {

    /**
     * 发送打洞申请
     */
    public void start(String remoteId) {
        HolePortSendMessage holePortApplyTransfer = new HolePortSendMessage();
        try {
            TransferMessage transferInfo = new TransferMessage();
            transferInfo.setHeader(TCPStatusEnum.APPLY_TRANSFER);
            transferInfo.setIdFrom(InetUtil.getProperties("").getProperty("publicKeyFrom"));
            transferInfo.setIdTo(remoteId);
            transferInfo.setInsideIp(InetAddress.getLocalHost().getHostAddress());
            transferInfo.setInsidePort(8001);
            //发送打洞请求
            holePortApplyTransfer.start(transferInfo, InetAddress.getByName(""), 8613, InetAddress.getByName(""), 8001);
            //关闭连接
            holePortApplyTransfer.close();
            HolePortListener holePortListener = new HolePortListener();
            holePortListener.start(InetAddress.getByName(""), 1000, 8001);
            Management.manager.put(remoteId, holePortListener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
