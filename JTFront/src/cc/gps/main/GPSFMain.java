package cc.gps.main;


import cc.gps.config.Global;
import cc.net.CCSocketServer;
import cc.net.jt808.JT808FrontServer;
import cc.net.lzbus.LZBUSFrontServer;
import cc.net.lzbus.LZBUSWebServer;
import cc.net.lztaxi.LZTAXIFrontServer;
import cc.net.monitor.Mointor_IChannelPiple;
import cc.net.monitor.MonitorServer;;

public class GPSFMain extends Thread{
	public static void main(String[] args) throws Exception {
		new GPSFMain().start();
		
    }
	@Override
	public void run(){
		Global.initAll();
		//LZBUS前端监控
		//LZBUSFrontServer lzb=new LZBUSFrontServer();
		//lzb.start();
		//向WEB转发
		//LZBUSWebServer lzw=new LZBUSWebServer();
		//lzw.start();
		//向监控平台转发
		//MonitorServer ms=new MonitorServer();
		//ms.start();
		//交通部
		JT808FrontServer jtf=new JT808FrontServer();
		jtf.start();
		//LZTAXI前端监控
		//LZTAXIFrontServer lzt=new LZTAXIFrontServer();
		//lzt.start();
	}
}
