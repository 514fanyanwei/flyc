package cc.net.monitor;
import cc.gps.config.Config;
import cc.net.CCSocketServer;

public class MonitorServer extends Thread{
	int port = Integer.parseInt(Config.getKey("monitorPort"));
	@Override
	public void run(){
		CCSocketServer msss=new CCSocketServer(port,new Mointor_IChannelPiple());
		try {
			msss.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}