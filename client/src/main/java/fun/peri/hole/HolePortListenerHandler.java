package fun.peri.hole;

import com.google.gson.Gson;
import fun.peri.constant.TCPStatusEnum;
import fun.peri.message.*;
import fun.peri.util.InetUtil;

import java.io.*;
import java.net.Socket;

/**
 * 转为监听器后实现类，若收到远端第一次握手请求，则响应予以回复第二次握手
 * 若收到远端第三次握手请求，则可以开始接收文件
 *
 * @author hellobosom@gmail.com
 */
public class HolePortListenerHandler implements Runnable {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private int byteRead;
    private volatile int start = 0;
    private String fileDir = "F:";

    public HolePortListenerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            byte[] bytes = new byte[1024];
            int temp;
            outputStream = new ByteArrayOutputStream();
            while ((temp = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, temp);
            }
            String str = outputStream.toString();
            //TODO
            BaseMessage message = new Gson().fromJson(str, BaseMessage.class);
            //收到远端第一次握手消息
            if (message instanceof FirstHandshakeMessage) {
                FirstHandshakeMessage firstHandshakeMessage = (FirstHandshakeMessage) message;
                dealFirstHandshakeMessage(firstHandshakeMessage);
            }
            //收到远端第三次握手消息
            if (message instanceof ThirdHandshakeMessage) {
                ThirdHandshakeMessage thirdHandshakeMessage = (ThirdHandshakeMessage) message;
                dealThirdHandshakeMessage(thirdHandshakeMessage);
            }
            if (message instanceof FileMessage) {
                dealFileMessage((FileMessage) message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dealFirstHandshakeMessage(FirstHandshakeMessage firstHandshakeMessage) {
        if (TCPStatusEnum.FIRST_HANDSHAKE.toString().equals(firstHandshakeMessage.getHeader())) {
            //向远端发送第二次握手消息
            SecondHandshakeMessage secondHandshakeMessage = new SecondHandshakeMessage();
            secondHandshakeMessage.setHeader(TCPStatusEnum.SECOND_HANDSHAKE);
            try {
                secondHandshakeMessage.setId(InetUtil.getProperties("").getProperty("localId"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO 发送第二次握手消息
        }
    }

    private void dealThirdHandshakeMessage(ThirdHandshakeMessage thirdHandshakeMessage) {
        if (TCPStatusEnum.THIRD_HANDSHAKE.toString().equals(thirdHandshakeMessage.getHeader())) {
            //TODO 接收文件


        }
    }

    void dealFileMessage(FileMessage fileMessage) {
        //TODO 判断开始结束消息
        byte[] bytes = fileMessage.getBytes();
        byteRead = fileMessage.getEndPos();
        String md5 = fileMessage.getFileMD5();
        String path = fileDir + File.separator + md5;
        File file = new File(path);
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            start = start + byteRead;
            System.out.println("path:" + path + "," + byteRead);
            if (byteRead > 0) {
                //TODO
                outputStream.write(start);
                randomAccessFile.close();

            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
