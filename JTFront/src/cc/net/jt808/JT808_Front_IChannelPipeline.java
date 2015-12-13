package cc.net.jt808;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import cc.gps.config.Global;
import cc.net.IChannelPipeline;
import cc.net.ICreatCheckPacket;
import cc.net.IdleHandler;
import cc.net.Platform_CreateCheckPacket;
import cc.net.jt808.GPSProcessVehicleHandler;
import cc.net.lzbus.LZBUS_GPSDecoder;


public class JT808_Front_IChannelPipeline implements IChannelPipeline{

	
	@Override
	public void initPipeline(SocketChannel ch) throws Exception {
		ICreatCheckPacket iccp=new JT808_Front_CreateCheckPacket();
		ch.pipeline().addLast(
			new JT_GPSDecoder(),
			//暂时关闭，正式使用时打开
       		// new IdleStateHandler(120, 60, 0),//读120s(终端最长不超过指定时间发送，否则认为下线) ,写(服务器发送最大空闲检查周期，超过则下发心跳)60s
       		// new IdleHandler(iccp,Global.CLIENTID),
       		 new GPSProcessVehicleHandler());
	}

}
