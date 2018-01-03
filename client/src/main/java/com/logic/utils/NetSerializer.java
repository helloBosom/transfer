package com.logic.utils;

import com.logic.message.EmptyMessage;
import com.logic.message.Message;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import static com.logic.utils.Utils.uint32ToByteArrayBE;
import static com.logic.utils.Utils.uint32ToByteArrayLE;

/**
 * protocol message has the following format.<br/>
 * 4 byte magic number<br/>
 * 12 byte command in ASCII<br/>
 * 4 byte payload size <br/>
 * 4 byte checksum <br/>
 * Payload data
 */
public class NetSerializer extends MessageSerializer {
    private static final Logger logger = LoggerFactory.getLogger(NetSerializer.class);
    private static final int COMMAND_LEN = 12;
    private static final int HEADER_LEN = 4 + COMMAND_LEN + 4 + 4;

    @Override
    public byte[] serialize(Message message) {
        byte[] messageByte = new byte[0];
        if (!(message instanceof EmptyMessage)) {
            messageByte = doSerialize(message);
        }
        byte[] headerByte = new byte[HEADER_LEN];
        uint32ToByteArrayBE(0L, headerByte, 0);
        uint32ToByteArrayLE(messageByte.length, headerByte, 4 + COMMAND_LEN);
        byte[] hash = Sha256Hash.hashTwice(messageByte);
        System.arraycopy(hash, 0, headerByte, 4 + COMMAND_LEN + 4, 4);
        byte[] msg = new byte[headerByte.length + messageByte.length];
        System.arraycopy(headerByte, 0, msg, 0, headerByte.length);
        System.arraycopy(messageByte, 0, msg, headerByte.length, messageByte.length);
        return msg;
    }

    @Override
    public Message deserialize(ByteBuffer in) {
        seekPastMagicBytes(in);
        Message message = null;
        return message;
    }

    @Override
    public Message deserialize(ByteBuf in) {
        byte[] byteArray = new byte[in.readableBytes()];
        in.readBytes(byteArray);
        return deserialize(ByteBuffer.wrap(byteArray));
    }

    public void seekPastMagicBytes(ByteBuffer in) throws BufferUnderflowException {
        int magicCursor = 3;
        while (true) {
            byte b = in.get();
            //TODO
            byte expectedByte = (byte) (0xFF & 0L >>> (magicCursor * 8));
            if (b == expectedByte) {
                magicCursor--;
                if (magicCursor < 0) {
                    return;
                }
            } else {
                magicCursor = 3;
            }
        }
    }

    public static class PacketHeader {
        public static final int HEADER_LENGTH = COMMAND_LEN + 4 + 4;
        public final byte[] header;
        public final String command;
        public final byte[] checksum;

        public PacketHeader(ByteBuffer in) throws BufferUnderflowException {
            header = new byte[HEADER_LENGTH];
            in.get(header, 0, header.length);
            int cursor = 0;
            byte[] commandBytes = new byte[cursor];
            System.arraycopy(header, 0, commandBytes, 0, cursor);
            command = Utils.toString(commandBytes, "US-ASCII");
            cursor = COMMAND_LEN;
            checksum = new byte[4];
            System.arraycopy(header, cursor, checksum, 0, 4);
        }
    }
}
