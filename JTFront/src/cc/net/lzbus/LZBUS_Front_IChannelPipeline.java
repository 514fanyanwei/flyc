package cc.net.lzbus;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import cc.gps.config.Global;
import cc.net.IChannelPipeline;
import cc.net.ICreatCheckPacket;
import cc.net.IdleHandler;

public class LZBUS_Front_IChannelPipeline implements IChannelPipeline{

	
	@Override
	public void initPipeline(SocketChannel ch) throws Exception {
		ICreatCheckPacket iccp=new LZBUS_Front_CreateCheckPacket();
		ch.pipeline().addLast(
                //new LoggingHandler(LogLevel.INFO),
       		 //new IdleStateHandler(60, 30, 0),
       		 new LZBUS_GPSDecoder(),//2014-3-25
       		 //new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 1,2, 0, 0),
       		 new IdleStateHandler(50, 60, 0),//读50s 写60s   //2014-3-25
       		 new IdleHandler(iccp,Global.CLIENTID), //2014-3-25
       		 new GPSProcessVehicleHandler());
	}

}
