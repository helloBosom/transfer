package fun.peri.utils;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Ints;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

public class Utils {

    private static final Joiner SPACE_JOINER = Joiner.on(" ");
    public static volatile Date mockTime;
    public static final BaseEncoding HEX = BaseEncoding.base16().lowerCase();

    public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        if (b == null) {
            return null;
        }
        byte[] bytes = new byte[numBytes];
        byte[] biBytes = b.toByteArray();
        int start = (biBytes.length == numBytes + 1) ? 1 : 0;
        int length = Math.min(biBytes.length, numBytes);
        System.arraycopy(biBytes, start, bytes, numBytes - length, length);
        return bytes;
    }

    public static byte[] getFileBytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static byte[] getFileBytes(String filePath) {
        return getFileBytes(new File(filePath));
    }

    public static void uint32ToByteArrayBE(long val, byte[] out, int offset) {
        out[offset] = (byte) (0xFF & (val >> 24));
        out[offset + 1] = (byte) (0xFF & (val >> 16));
        out[offset + 2] = (byte) (0xFF & (val >> 8));
        out[offset + 3] = (byte) (0xFF & val);
    }

    public static void uint32ToByteArrayLE(long val, byte[] out, int offset) {
        out[offset] = (byte) (0xFF & val);
        out[offset + 1] = (byte) (0xFF & (val >> 8));
        out[offset + 2] = (byte) (0xFF & (val >> 16));
        out[offset + 3] = (byte) (0xFF & (val >> 24));
    }

    public static void uint64ToByteArrayLE(long val, byte[] out, int offset) {
        out[offset] = (byte) (0xFF & val);
        out[offset + 1] = (byte) (0xFF & (val >> 8));
        out[offset + 2] = (byte) (0xFF & (val >> 16));
        out[offset + 3] = (byte) (0xFF & (val >> 24));
        out[offset + 4] = (byte) (0xFF & (val >> 32));
        out[offset + 5] = (byte) (0xFF & (val >> 40));
        out[offset + 6] = (byte) (0xFF & (val >> 48));
        out[offset + 7] = (byte) (0xFF & (val >> 56));
    }

    public static void uint32ToByteStreamLE(long val, OutputStream stream) throws IOException {
        stream.write((int) (0xFF & val));
        stream.write((int) (0xFF & (val >> 8)));
        stream.write((int) (0xFF & (val >> 16)));
        stream.write((int) (0xFF & (val >> 24)));
    }

    public static void int64ToByteStreamLE(long val, OutputStream stream) throws IOException {
        stream.write((int) (0xFF & val));
        stream.write((int) (0xFF & (val >> 8)));
        stream.write((int) (0xFF & (val >> 16)));
        stream.write((int) (0xFF & (val >> 24)));
        stream.write((int) (0xFF & (val >> 32)));
        stream.write((int) (0xFF & (val >> 40)));
        stream.write((int) (0xFF & (val >> 48)));
        stream.write((int) (0xFF & (val >> 56)));
    }

    public static void uint64ToByteStreamLE(BigInteger val, OutputStream stream) throws IOException {
        byte[] bytes = val.toByteArray();
        if (bytes.length > 8) {
            throw new RuntimeException("Input too large to encode into a uint64");
        }
        bytes = reverseBytes(bytes);
        stream.write(bytes);
        if (bytes.length < 8) {
            for (int i = 0; i < 8 - bytes.length; i++) {
                stream.write(0);
            }
        }
    }

    public static byte[] reverseBytes(byte[] bytes) {
        byte[] buf = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            buf[i] = bytes[bytes.length - 1 - i];
        return buf;
    }

    public static byte[] reverseDwordBytes(byte[] bytes, int trimLength) {
        checkArgument(bytes.length % 4 == 0);
        checkArgument(trimLength < 0 || trimLength % 4 == 0);
        byte[] rev = new byte[trimLength >= 0 && bytes.length > trimLength ? trimLength : bytes.length];
        for (int i = 0; i < rev.length; i += 4) {
            System.arraycopy(bytes, i, rev, i, 4);
            for (int j = 0; j < 4; j++) {
                rev[i + j] = bytes[i + 3 - j];
            }
        }
        return rev;
    }

    public static long readUint32(byte[] bytes, int offset) {
        return (bytes[offset] & 0xffl) | ((bytes[offset + 1] & 0xffl) << 8) | ((bytes[offset + 2] & 0xffl) << 16)
                | ((bytes[offset + 3] & 0xffl) << 24);
    }

    public static long readInt64(byte[] bytes, int offset) {
        return (bytes[offset] & 0xffl) | ((bytes[offset + 1] & 0xffl) << 8) | ((bytes[offset + 2] & 0xffl) << 16)
                | ((bytes[offset + 3] & 0xffl) << 24) | ((bytes[offset + 4] & 0xffl) << 32)
                | ((bytes[offset + 5] & 0xffl) << 40) | ((bytes[offset + 6] & 0xffl) << 48)
                | ((bytes[offset + 7] & 0xffl) << 56);
    }

    public static long readUint32BE(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xffl) << 24) | ((bytes[offset + 1] & 0xffl) << 16)
                | ((bytes[offset + 2] & 0xffl) << 8) | (bytes[offset + 3] & 0xffl);
    }

    public static int readUint16BE(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xff) << 8) | (bytes[offset + 1] & 0xff);
    }

    public static BigInteger decodeMPI(byte[] mpi, boolean hasLength) {
        byte[] buf;
        if (hasLength) {
            int length = (int) readUint32BE(mpi, 0);
            buf = new byte[length];
            System.arraycopy(mpi, 4, buf, 0, length);
        } else
            buf = mpi;
        if (buf.length == 0)
            return BigInteger.ZERO;
        boolean isNegative = (buf[0] & 0x80) == 0x80;
        if (isNegative)
            buf[0] &= 0x7f;
        BigInteger result = new BigInteger(buf);
        return isNegative ? result.negate() : result;
    }

    public static byte[] encodeMPI(BigInteger value, boolean includeLength) {
        if (value.equals(BigInteger.ZERO)) {
            if (!includeLength)
                return new byte[]{};
            else
                return new byte[]{0x00, 0x00, 0x00, 0x00};
        }
        boolean isNegative = value.signum() < 0;
        if (isNegative)
            value = value.negate();
        byte[] array = value.toByteArray();
        int length = array.length;
        if ((array[0] & 0x80) == 0x80)
            length++;
        if (includeLength) {
            byte[] result = new byte[length + 4];
            System.arraycopy(array, 0, result, length - array.length + 3, array.length);
            uint32ToByteArrayBE(length, result, 0);
            if (isNegative)
                result[4] |= 0x80;
            return result;
        } else {
            byte[] result;
            if (length != array.length) {
                result = new byte[length];
                System.arraycopy(array, 0, result, 1, array.length);
            } else
                result = array;
            if (isNegative)
                result[0] |= 0x80;
            return result;
        }
    }

    public static BigInteger decodeCompactBits(long compact) {
        int size = ((int) (compact >> 24)) & 0xFF;
        byte[] bytes = new byte[4 + size];
        bytes[3] = (byte) size;
        if (size >= 1)
            bytes[4] = (byte) ((compact >> 16) & 0xFF);
        if (size >= 2)
            bytes[5] = (byte) ((compact >> 8) & 0xFF);
        if (size >= 3)
            bytes[6] = (byte) (compact & 0xFF);
        return decodeMPI(bytes, true);
    }

    public static long encodeCompactBits(BigInteger value) {
        long result;
        int size = value.toByteArray().length;
        if (size <= 3)
            result = value.longValue() << 8 * (3 - size);
        else
            result = value.shiftRight(8 * (size - 3)).longValue();
        if ((result & 0x00800000L) != 0) {
            result >>= 8;
            size++;
        }
        result |= size << 24;
        result |= value.signum() == -1 ? 0x00800000 : 0;
        return result;
    }

    public static <T> String join(Iterable<T> items) {
        if (null != items) {
            return SPACE_JOINER.join(items);
        }
        return null;
    }

    public static byte[] copyOf(byte[] in, int length) {
        byte[] out = new byte[length];
        System.arraycopy(in, 0, out, 0, Math.min(length, in.length));
        return out;
    }

    public static byte[] appendByte(byte[] bytes, byte b) {
        byte[] result = Arrays.copyOf(bytes, bytes.length + 1);
        result[result.length - 1] = b;
        return result;
    }

    public static String toString(byte[] bytes, String charsetName) {
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toBytes(CharSequence str, String charsetName) {
        try {
            return str.toString().getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date rollMockClock(int seconds) {
        return rollMockClockMillis(seconds * 1000);
    }

    public static Date rollMockClockMillis(long millis) {
        if (mockTime == null)
            throw new IllegalStateException("You need to use setMockClock() first.");
        mockTime = new Date(mockTime.getTime() + millis);
        return mockTime;
    }

    public static void setMockClock() {
        mockTime = new Date();
    }

    public static void setMockClock(long mockClockSeconds) {
        mockTime = new Date(mockClockSeconds * 1000);
    }

    public static Date now() {
        return mockTime != null ? mockTime : new Date();
    }

    public static long currentTimeMillis() {
        return mockTime != null ? mockTime.getTime() : System.currentTimeMillis();
    }

    public static long currentTimeSeconds() {
        return currentTimeMillis() / 1000;
    }

    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static String dateTimeFormat(Date dateTime) {
        DateFormat iso8601 = new SimpleDateFormat("yyyyMMdd'@'HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        iso8601.setTimeZone(UTC);
        return iso8601.format(dateTime);
    }

    public static String dateTimeFormat(long dateTime) {
        DateFormat iso8601 = new SimpleDateFormat("yyyyMMdd'@'HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        iso8601.setTimeZone(UTC);
        return iso8601.format(dateTime);
    }

    private static int isAndroid = -1;

    public static boolean isAndroidRuntime() {
        if (isAndroid == -1) {
            final String runtime = System.getProperty("java.runtime.name");
            isAndroid = (runtime != null && runtime.equals("Android Runtime")) ? 1 : 0;
        }
        return isAndroid == 1;
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static final String ZHANGWEIQIANG_SIGNED_MESSAGE_HEADER = "Zhangweiqiang Signed Message:\n";
    public static final byte[] ZHANGWEIQIANG_SIGNED_MESSAGE_HEADER_BYTES = ZHANGWEIQIANG_SIGNED_MESSAGE_HEADER.getBytes(Charsets.UTF_8);

    public static byte[] formatMessageForSigning(String message) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(ZHANGWEIQIANG_SIGNED_MESSAGE_HEADER_BYTES.length);
            bos.write(ZHANGWEIQIANG_SIGNED_MESSAGE_HEADER_BYTES);
            byte[] messageBytes = message.getBytes(Charsets.UTF_8);
            bos.write(messageBytes.length);
            bos.write(messageBytes);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);  // Cannot happen.
        }
    }

    public static int getRandomPort() {
        int max = 65535;
        int min = 1024;
        Random random = new Random();
        int port = random.nextInt(max) % (max - min + 1) + min;
        try {
            new Socket("localhost", port);
            return getRandomPort();
        } catch (UnknownHostException e) {
            return -1;
        } catch (IOException e) {
            return port;
        }
    }

    public static String getLocalIP() {
        try {
            if (isWindowsOS()) {
                return InetAddress.getLocalHost().getHostAddress();
            } else {
                return getLinuxLocalIp();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String genUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean isInner(String ip) {
        String reg = "(10|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(ip);
        return matcher.find();
    }

    public static boolean isSameSegment(String ip1, String ip2) {
        return Ipv4Util.checkSameSegmentByDefault(ip1, ip2);
    }

    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取本地Host名称
     */
    public static String getLocalHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                                System.out.println(ipaddress);
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("获取ip地址异常");
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        System.out.println("IP:" + ip);
        return ip;
    }

    public static InetAddress getLocalInetAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return null;
        }
    }


    public static String formatSocketAddress(InetSocketAddress remote) {
        return "[" + remote.getAddress().getHostAddress() + "]:" + remote.getPort();
    }

    public static boolean lockFile(File file) {
        try {
            boolean exists = file.exists();
            if (!exists) {
                return false;
            }
            FileChannel channel = (new RandomAccessFile(file, "rw")).getChannel();
            FileLock fileLock = channel.tryLock(0, 2, true);
            if (fileLock == null) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isFileLocked(File file) {
        RandomAccessFile file2 = null;
        try {
            if (!file.exists()) {
                return false;
            }
            if (file.isDirectory()) {
                return false;
            }
            file2 = new RandomAccessFile(file, "rw");
            FileLock lock = file2.getChannel().tryLock();
            if (lock == null) {
                return true;
            }
            lock.release();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file2 != null)
                try {
                    file2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return false;
    }

    private static void createIfNotExist(String filepath) throws IOException {
        String folder = filepath.substring(0, filepath.lastIndexOf(File.separator));
        if (!new File(folder).exists()) {
            new File(folder).mkdirs();
        }
        if (!new File(filepath).exists()) {
            new File(filepath).createNewFile();
        }
    }

    public static <T> T getObjFromFileDeserialize(String fileName, Class<T> targetClass) {
        try {
            createIfNotExist(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] payload = getFileBytes(fileName);
        if (payload != null) {
            return deserialize(payload, targetClass);
        } else {
            return null;
        }
    }

    public static <T> void createFileFromObj(T obj, String fileName) {
        createFile(serialize(obj), fileName);
    }

    public static void createFile(byte[] out, String fileName) {
        FileOutputStream fop = null;
        File file;
        try {
            if (fileName.contains(File.separator)) {
                String folder = fileName.substring(0, fileName.lastIndexOf(File.separator));
                File f = new File(folder);
                if (!f.exists()) {
                    f.mkdirs();
                }
            }
            file = new File(fileName);
            fop = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            fop.write(out);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new RuntimeException("serialize(" + obj + ")!");
        }
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] protostuff;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);

        } catch (Exception e) {
            throw new RuntimeException("serialize(" + obj.getClass() + ") object (" + obj + ") exception!", e);
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    public static <T> T deserialize(byte[] payload, Class<T> targetClass) {
        Objenesis objenesis = new ObjenesisStd(true);
        T instance = null;
        if (payload != null && payload.length != 0) {
            instance = objenesis.newInstance(targetClass);
            Schema<T> schema = RuntimeSchema.getSchema(targetClass);
            ProtostuffIOUtil.mergeFrom(payload, instance, schema);
        }
        return instance;
    }

    public static <T> byte[] serializeList(List<T> objList) {
        if (objList == null || objList.isEmpty()) {
            throw new RuntimeException("serialize list (" + objList + ") exception!");
        }
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(objList.get(0).getClass());
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

    public static <T> List<T> deserializeList(byte[] payload, Class<T> targetClass) {
        if (payload == null || payload.length == 0) {
            throw new RuntimeException("deserialize exception, byte is null!");
        }
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        List<T> result = null;
        try {
            result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(payload), schema);
        } catch (IOException e) {
            throw new RuntimeException("deserialize exception!", e);
        }
        return result;
    }

    private static class Pair implements Comparable<Pair> {
        int item, count;

        public Pair(int item, int count) {
            this.count = count;
            this.item = item;
        }

        @Override
        public int compareTo(Pair o) {
            return -Ints.compare(count, o.count);
        }
    }

    public static int maxOfMostFreq(int... items) {
        ArrayList<Integer> list = new ArrayList<>(items.length);
        for (int item : items) list.add(item);
        return maxOfMostFreq(list);
    }

    public static int maxOfMostFreq(List<Integer> items) {
        if (items.isEmpty()) {
            return 0;
        }
        items = Ordering.natural().reverse().sortedCopy(items);
        LinkedList<Pair> pairs = Lists.newLinkedList();
        pairs.add(new Pair(items.get(0), 0));
        for (int item : items) {
            Pair pair = pairs.getLast();
            if (pair.item != item)
                pairs.add((pair = new Pair(item, 0)));
            pair.count++;
        }
        Collections.sort(pairs);
        int maxCount = pairs.getFirst().count;
        int maxItem = pairs.getFirst().item;
        for (Pair pair : pairs) {
            if (pair.count != maxCount) {
                break;
            }
            maxItem = Math.max(maxItem, pair.item);
        }
        return maxItem;
    }

}
