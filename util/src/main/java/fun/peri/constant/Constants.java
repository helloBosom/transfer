package fun.peri.constant;

/**
 * 常量
 */
public enum Constants {
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