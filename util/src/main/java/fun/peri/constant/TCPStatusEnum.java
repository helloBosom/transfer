package fun.peri.constant;

/**
 * @author hellobosom@gmail.com
 * 常量
 * 枚举就是特殊的常量类，且构造方法被默认强制是私有
 */
public enum TCPStatusEnum {
    /**
     * 三次握手
     */
    FIRST_HANDSHAKE,
    SYN_CENT,
    SECOND_HANDSHAKE,
    SYN_RECV,
    THIRD_HANDSHAKE,
    ESTABLISHED,
    /**
     * 四次挥手
     */
    FIRST_GOODBYE,
    SECOND_GOODBYE,
    THIRD_GOODBYE,
    FORTH_GOODBYE,
    ACK,
    FIN,
    /**
     * 连接状态
     */
    TIME_WAIT,
    CONNECT,
    APPLY_TRANSFER,
    SENDFILE,
}