package cc.gps.util;
import java.io.ByteArrayInputStream;    
import java.io.ByteArrayOutputStream;    
import java.io.IOException;    
import java.io.InputStream;    
import java.io.ObjectInputStream;    
import java.io.ObjectOutputStream;    
import java.nio.ByteBuffer;    
 
    
/**   
 * 对象序列化，反序列化（序列化对象转byte[],ByteBuffer, byte[]转object   
 *    
 * @author Vicky   
 * @email eclipser@163.com   
 */    
public class ByteUtil {    
    
    public static byte[] toBytes(Object obj) throws IOException {    
        ByteArrayOutputStream bout = new ByteArrayOutputStream();    
        ObjectOutputStream out = new ObjectOutputStream(bout);    
        out.writeObject(obj);    
        out.flush();    
        byte[] bytes = bout.toByteArray();    
        bout.close();    
        out.close();    
        return bytes;    
    }    
    
    public static Object toObject(byte[] bytes) throws IOException   
, ClassNotFoundException {    
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);    
        ObjectInputStream oi = new ObjectInputStream(bi);    
        Object obj = oi.readObject();    
        bi.close();    
        oi.close();    
        return obj;    
    }    
    
    public static Object toObject(ByteBuffer byteBuffer) throws ClassNotFoundException, IOException {    

    	byte[] bs=byteBuffer.array();
    	Object obj=toObject(bs);
        return obj;    
    }    
    
    public static ByteBuffer toByteBuffer(Object obj) throws IOException {    
        byte[] bytes = ByteUtil.toBytes(obj);    
        ByteBuffer buff = ByteBuffer.wrap(bytes);    
        return buff;    
    }    
    
}   
