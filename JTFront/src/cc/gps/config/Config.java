package cc.gps.config;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import cc.gps.data.Recourse;

public class Config {
	
	private static Properties prop = null;
	private Config(){
		InputStream fis =Recourse.class.getClassLoader().getResourceAsStream("config.properties");
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
	
	public static String getKey(String orderid){
		if(prop==null)		new Config();
		return (String)prop.get(orderid);
	}
	
}
