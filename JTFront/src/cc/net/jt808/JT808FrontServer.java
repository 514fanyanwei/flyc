package cc.net.jt808;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.config.Config;
import cc.net.CCSocketServer;


public class JT808FrontServer  extends Thread{
	private static final Log log = LogFactory.getLog(JT808FrontServer.class);
	int port = Integer.parseInt(Config.getKey("monitorPort"));
	@Override
	public void run(){
		log.info("启动交通部标准设备数据采集");
		//int port = Integer.parseInt(Config.getKey("frontPort"));
		int port = 39990;
		//int port = 36660;
		CCSocketServer frontJTss=new CCSocketServer(port,new JT808_Front_IChannelPipeline());
		try {
			frontJTss.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}