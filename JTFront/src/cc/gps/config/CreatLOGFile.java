package cc.gps.config;

import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import cc.gps.util.DateUtil;

public class CreatLOGFile  extends TimerTask {
	
	@Override
	public void run() {
		RandomAccessFile file=null;
		//int n=(int)(Math.random()*100);
		try {
			file = new RandomAccessFile (Config.getKey("GPSLOGPath")+DateUtil.GetDate()+".log", "rw");
			Global.FC = file.getChannel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		String s=DateUtil.GetDate();
		
		
		RandomAccessFile file=null;
		try {
			file = new RandomAccessFile (Config.getKey("GPSLOGPath")+s+".log", "rw");
			Global.FC = file.getChannel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
