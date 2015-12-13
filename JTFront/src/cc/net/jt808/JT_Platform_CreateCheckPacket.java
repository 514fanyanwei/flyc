package cc.net.jt808;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.X2CData;
import cc.gps.service.MonitorSessionManager;
import cc.gps.util.SerializeUtil;
import cc.net.ICreatCheckPacket;
import cc.net.Platform_CreateCheckPacket;

public class JT_Platform_CreateCheckPacket implements ICreatCheckPacket{
	private static final Log log = LogFactory.getLog(JT_Platform_CreateCheckPacket.class);
	
	@Override
	public void createHeartBeat(ChannelHandlerContext ctx)  {
		/*
		byte[] bs=null;
		X2CData request = new X2CData(0x0102,"","front","",1);
    	try {
			bs=SerializeUtil.serializeObject(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }

	//点名
	@Override
	public void createRollCall(ChannelHandlerContext ctx) {
		byte[] bs=null;
		X2CData request = new X2CData(0x0102,"","front","",1);
		int count=ctx.channel().attr(Global.CALLCOUNT).get();
    	try {
			bs=SerializeUtil.serializeObject(request);
			ctx.channel().attr(Global.CALLCOUNT).set(++count);
			ctx.writeAndFlush(bs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void down(String clientID) {
		// TODO Auto-generated method stub
		log.info("监控已下线");
		//log.info("置"+clientID+"终端下线");
		log.info(MonitorSessionManager.getCacheMap().size()+"*******");
		MonitorSessionManager.invalidate(clientID);
		log.info(MonitorSessionManager.getCacheMap().size());
		
	}

}
