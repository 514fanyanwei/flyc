package cc.gps.active.lztaxi;

import io.netty.channel.ChannelHandlerContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.lzbus.LZBusProcessDown;
import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.lztaxi.LZTAXI0x4c;
import cc.gps.service.SocketSessionManager;
import cc.gps.util.Cache;

public class LZTAXIProcess0x6c extends LZTAXIProcessReceive{
	private static final Log log = LogFactory.getLog(LZTAXIProcess0x4C.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lztaxi006c")+":-------------");
		String clientID=inData.head.phone;
		log.info("终端"+clientID+"主动下线");
		Cache cache=(Cache)SocketSessionManager.getContent(clientID);
		if(cache!=null){
			ChannelHandlerContext ctx=(ChannelHandlerContext)(cache.getValue());
			ctx.close();
		}
		SocketSessionManager.invalidate(clientID);
		Global.CID2ONOFF.put(clientID, 0);  //先置下线
		
		return null;
	}
}