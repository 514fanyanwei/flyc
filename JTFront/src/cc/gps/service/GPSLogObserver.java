package cc.gps.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.queue.GPSQueue;
import cc.gps.util.Ecode;

public class GPSLogObserver implements Observer{

	@Override
	public void update(Observable o, Object arg) {
    	GPSQueue<String> queue =(GPSQueue)arg;
    	StringBuffer sb=new StringBuffer();
    	while(queue.size()>0){
    		 String str=(String)queue.remove();
    		 sb.append(str);
    	}
    	byte[] bs=(sb.toString()).getBytes();
    	try {
			MappedByteBuffer mbb = Global.FC.map (FileChannel.MapMode.READ_WRITE, Global.FC.size(), bs.length);
			mbb.put(bs);
			mbb.flip();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
