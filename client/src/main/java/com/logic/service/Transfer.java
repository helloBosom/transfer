package com.logic.service;

import com.logic.hole.HolePortSendMessage;
import com.logic.hole.HolePortListener;
import com.logic.manager.Management;
import com.logic.message.TransferMessage;
import com.logic.utils.InetUtil;

import java.io.IOException;
import java.net.InetAddress;

/**
 * 接口实现类
 */
public class Transfer{
    /**
     * 发送打洞申请
     *
     * @param remoteId
     */
    public void start(String remoteId) {
        HolePortSendMessage holePortApplyTransfer = new HolePortSendMessage();
        try {
            TransferMessage transferInfo = new TransferMessage();
            transferInfo.setHeader("applyTransfer");
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
