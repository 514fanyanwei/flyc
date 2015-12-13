package cc.net.lztaxi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Config;
import cc.net.CCSocketServer;
import cc.net.jt808.JT808FrontServer;
import cc.net.monitor.Mointor_IChannelPiple;

public class LZTAXIFrontServer  extends Thread{
	private static final Log log = LogFactory.getLog(LZTAXIFrontServer.class);
	//int port = Integer.parseInt(Config.getKey("monitorPort"));
	@Override
	public void run(){
		//int port = Integer.parseInt(Config.getKey("frontPort"));
		log.info("启动蓝斯出租车设备数据采集");
		//int port=20011;
		int port=39990;
		CCSocketServer frontLZss=new CCSocketServer(port,new LZTAXI_Front_IChannelPipeline());
		try {
			frontLZss.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}