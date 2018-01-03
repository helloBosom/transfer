package com.logic.utils;

import com.logic.message.Message;
import io.netty.buffer.ByteBuf;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MessageSerializer {
    private static Objenesis objenesis = new ObjenesisStd(true);
    protected static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    public abstract byte[] serialize(Message message) throws UnsupportedOperationException;

    public abstract Message deserialize(ByteBuffer in);

    public abstract Message deserialize(ByteBuf in);

    protected <T> byte[] doSerialize(T obj) {
        if (obj == null) {
            throw new RuntimeException("serialize(" + obj + ")!");
        }
        Schema<T> schema = (Schema<T>) getSchema(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] protostuff = null;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new RuntimeException("serialize(" + obj.getClass() + ") object (" + obj + ") exception!", e);
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    protected <T> T deserialize(byte[] payload, Class<T> targetClass) {
        if (payload == null || payload.length == 0) {
            throw new RuntimeException("deserialize exception, byte is null!");
        }
        T instance;
        instance = objenesis.newInstance(targetClass);
        Schema<T> schema = getSchema(targetClass);
        ProtostuffIOUtil.mergeFrom(payload, instance, schema);
        return instance;
    }

    protected <T> byte[] serializeList(List<T> objList) {
        if (objList == null || objList.isEmpty()) {
            throw new RuntimeException("serialize list (" + objList + ") exception!");
        }
        Schema<T> schema = (Schema<T>) getSchema(objList.get(0).getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] protostuff;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
            protostuff = bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("serialize list (" + objList + ") exception!", e);
        } finally {
            buffer.clear();
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return protostuff;
    }

    protected <T> List<T> deserializeList(byte[] payload, Class<T> targetClass) {
        if (payload == null || payload.length == 0) {
            throw new RuntimeException("deserialize exception, byte is null!");
        }
        Schema<T> schema = getSchema(targetClass);
        List<T> result = null;
        try {
            result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(payload), schema);
        } catch (IOException e) {
            throw new RuntimeException("deserialize exception!", e);
        }
        return result;
    }

    private <T> Schema<T> getSchema(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            if (schema != null) {
                cachedSchema.put(clazz, schema);
            }
        }
        return schema;
    }
}