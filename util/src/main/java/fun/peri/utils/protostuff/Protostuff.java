package fun.peri.utils.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.lang.reflect.InvocationTargetException;

/**
 * protostuff
 */
public class Protostuff {

    static <T> byte[] serializer(T clazz) {
        Schema schema = RuntimeSchema.getSchema(clazz.getClass());
        return ProtobufIOUtil.toByteArray(clazz, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    static <T> T deserializer(byte[] bytes, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
            Schema schema = RuntimeSchema.getSchema(obj.getClass());
            ProtobufIOUtil.mergeFrom(bytes, obj, schema);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
