package cc.net.lzbus;

import cc.gps.config.Config;
import cc.net.CCSocketServer;
import cc.net.monitor.Mointor_IChannelPiple;

public class LZBUSFrontServer  extends Thread{
	int port = Integer.parseInt(Config.getKey("monitorPort"));
	@Override
	public void run(){
		int port = Integer.parseInt(Config.getKey("frontPort"));
		//int port=38880;
		CCSocketServer frontLZss=new CCSocketServer(port,new LZBUS_Front_IChannelPipeline());
		try {
			frontLZss.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}