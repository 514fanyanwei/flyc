package cc.net.lzbus;

import cc.gps.config.Config;
import cc.net.CCSocketServer;

public class LZBUSWebServer  extends Thread{
	@Override
	public void run(){
		int port = Integer.parseInt(Config.getKey("forwardPort"));
		CCSocketServer webLZss=new CCSocketServer(port,new LZBUS_Web_IChannelPipeline());
		try {
			webLZss.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}