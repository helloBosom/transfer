package fun.peri.hole;

import com.google.gson.Gson;
import fun.peri.constant.TCPStatusEnum;
import fun.peri.message.*;
import fun.peri.util.InetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * 接收到第二次握手，予以回复第三次握手消息
 * sleep后,开始发送文件
 *
 * @author hellobosom@gmail.com
 */
public class HolePortReceiveHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HolePortReceiveHandler.class);
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String fileLocation;

    private int byteRead;
    private volatile int start = 0;
    private volatile int lastLength = 0;
    public RandomAccessFile randomAccessFile;
    private FileMessage fileUploadFile;

    public HolePortReceiveHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int temp;
            while ((temp = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, temp);
            }
            String str = outputStream.toString();
            //TODO
            BaseMessage message = new Gson().fromJson(str, BaseMessage.class);
            //第二次握手
            if (message instanceof SecondHandshakeMessage) {
                SecondHandshakeMessage secondHandshakeMessage = (SecondHandshakeMessage) message;
                dealSecondHandshakeMessage(secondHandshakeMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void dealSecondHandshakeMessage(SecondHandshakeMessage secondHandshakeMessage) {
        if (TCPStatusEnum.SECOND_HANDSHAKE.toString().equals(secondHandshakeMessage.getHeader())) {
            ThirdHandshakeMessage thirdHandshakeMessage = new ThirdHandshakeMessage();
            thirdHandshakeMessage.setHeader(TCPStatusEnum.THIRD_HANDSHAKE);
            try {
                thirdHandshakeMessage.setId(InetUtil.getProperties("").getProperty("localId"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO 发送第三次握手消息

            //TODO 发送文件
            String fileLocation = "";
            sendFile(fileLocation);
        }
    }

    /**
     * 发送文件
     *
     * @param sendFileMessage
     */
    private void dealSendFileMessage(SendFileMessage sendFileMessage) {
        if (TCPStatusEnum.SENDFILE.equals(sendFileMessage.getHeader())) {
            File download = new File(fileLocation);
            try {
                randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
                randomAccessFile.seek(fileUploadFile.getStarPos());
                // lastLength = (int) randomAccessFile.length() / 10;
                lastLength = 1024 * 10;
                byte[] bytes = new byte[lastLength];
                if ((byteRead = randomAccessFile.read(bytes)) != -1) {
                    fileUploadFile.setEndPos(byteRead);
                    fileUploadFile.setBytes(bytes);
                } else {
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    void sendFile(String fileLocation) {
        if (start != -1) {
            try {
                randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
                randomAccessFile.seek(start);
                logger.info("长度：" + (randomAccessFile.length() - start));
                int a = (int) (randomAccessFile.length() - start);
                int b = (int) (randomAccessFile.length() / 1024 * 2);
                if (a < lastLength) {
                    lastLength = a;
                }
                logger.info("文件长度：" + (randomAccessFile.length()) + ",start:" + start + ",a:" + a + ",b:" + b + ",lastLength:" + lastLength);
                byte[] bytes = new byte[lastLength];
                if ((byteRead = randomAccessFile.read(bytes)) != -1
                        && (randomAccessFile.length() - start) > 0) {
                    fileUploadFile.setEndPos(byteRead);
                    fileUploadFile.setBytes(bytes);
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
