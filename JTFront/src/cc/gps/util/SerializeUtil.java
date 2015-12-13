package cc.gps.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class SerializeUtil { 
	/** 序列化对象 

     * @throws IOException 
     * */ 

    public static byte[] serializeObject(Object object) throws IOException{ 
       ByteArrayOutputStream out=new ByteArrayOutputStream(); 
       ObjectOutputStream oos=new ObjectOutputStream(out);
       oos.writeObject(object);
       oos.flush();
       return out.toByteArray(); 
     }
    /** 反序列化对象 

     * ＠throws IOException 

     * ＠throws ClassNotFoundException */ 

    public static Object deserializeObject(byte[]buf) throws IOException,ClassNotFoundException{ 

       Object object=null; 

       ByteArrayInputStream sais=new ByteArrayInputStream(buf); 

       ObjectInputStream ois = new ObjectInputStream(sais); 

       object=(Object)ois.readObject();

       return object; 

    } 

}
