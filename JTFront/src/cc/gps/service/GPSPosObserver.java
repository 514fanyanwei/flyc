package cc.gps.service;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.queue.GPSQueue;
import cc.gps.service.BusinessSessionManager;
import cc.gps.util.Cache;


public class GPSPosObserver implements Observer{
	private static final Log log = LogFactory.getLog(GPSPosObserver.class);
	@Override
	public void update(Observable o, Object arg) {
		GPSQueue<JTReceiveData> queue =(GPSQueue)arg;
		HashMap<String,Cache> map = BusinessSessionManager.getCacheMap();
		
		while(queue.size()>0){
			JTReceiveData data=queue.remove();
			//log.info(queue.size()+"----");
			for (String webID:map.keySet()) 
			{
				//log.debug("向"+webID+"转发");
				 Cache cache=(BusinessSessionManager.getContent(webID));
				 ChannelHandlerContext ctx=(ChannelHandlerContext)cache.getValue();
				 ctx.writeAndFlush(data);
				 //break; //仅需发送1次 //待仔细研究
		    }
		}
	}

}
