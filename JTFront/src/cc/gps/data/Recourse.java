package cc.gps.data;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.Parse;

public class Recourse {
	//private static String filePath="D:\\citycard\\java\\gps1\\src\\order.properties";
	private static final Log log = LogFactory.getLog(Recourse.class);
	private static Properties prop = null;
	
	private Recourse(){
		InputStream fis =Recourse.class.getClassLoader().getResourceAsStream("order.properties");
		if(prop==null){
			prop = new Properties();
			InputStream in;
			try {
				in = new BufferedInputStream (fis);
				prop.load(in);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String getOrder(String orderid){
		if(prop==null)	new Recourse();
		return (String)prop.get(orderid);
	}
	
	public static void main(String args[]) throws IOException{
		Properties prop = new Properties();
		String filePath="D:\\citycard\\java\\gps1\\src\\test.properties";
		//InputStream in = Class.getResourceAsStream("/a.properties");
		InputStream in = new BufferedInputStream (new FileInputStream(filePath));
		prop.load(in);
		Set keyValue = prop.keySet();
		for (Iterator it = keyValue.iterator(); it.hasNext();)
		{
			String key = (String) it.next();
			log.info(key+":"+prop.get(key));
		}
		
		 OutputStream fos = new FileOutputStream(filePath);
         prop.setProperty("111", "bbb");
         //以适合使用 load 方法加载到 Properties 表中的格式，
         //将此 Properties 表中的属性列表（键和元素对）写入输出流
         prop.store(fos, "Update '"  + "' value");

         long milisNow = new Date().getTime();
         log.info(milisNow);
	}

}
