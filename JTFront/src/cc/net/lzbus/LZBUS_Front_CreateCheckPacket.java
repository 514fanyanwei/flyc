package cc.net.lzbus;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import cc.gps.active.lzbus.LZBusBuildSendPacket;
import cc.gps.active.lzbus.LZBusBuildSendPacketFactory;
import cc.gps.active.lzbus.LZBusProcess0x42;
import cc.gps.active.lzbus.LZBusProcessDown;
import cc.gps.config.Global;
import cc.gps.data.jt808.JT0x9999;
import cc.gps.service.SocketSessionManager;
import cc.gps.util.Cache;
import cc.gps.util.Ecode;
import cc.net.ICreatCheckPacket;

public class LZBUS_Front_CreateCheckPacket implements ICreatCheckPacket{
	private static final Log log = LogFactory.getLog(LZBUS_Front_CreateCheckPacket.class);
	
	@Override
	public void createHeartBeat(ChannelHandlerContext ctx) {
		byte[] bs=null;
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x41); //心跳包
		bs=jtb.buildSendPacket(null); 
		ByteBuf bb=Unpooled.buffer(bs.length);
		bb.writeBytes(bs);
		ctx.writeAndFlush(bb);
		//return bs;
	}

	@Override
	public void createRollCall(ChannelHandlerContext ctx) {
		byte[] bs=null;
		int count=ctx.channel().attr(Global.CALLCOUNT).get();
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x9943); //点名包
		bs=jtb.buildSendPacket(null); 
		ByteBuf bb=Unpooled.buffer(bs.length);
		bb.writeBytes(bs);
		ctx.channel().attr(Global.CALLCOUNT).set(++count);
		ctx.writeAndFlush(bb);
		save2Queue(ctx.channel().attr(Global.CLIENTID).get(),bs);
	}

	@Override
	public void down(String clientID) {
		//置车辆下线状态
	  log.info("置"+clientID+"终端下线");
	  
	  Cache cache=(Cache)SocketSessionManager.getContent(clientID);
	  if(cache!=null){
			ChannelHandlerContext ctx=(ChannelHandlerContext)(cache.getValue());
			ctx.close();
	  }
	  
	  SocketSessionManager.invalidate(clientID);
	  Global.CID2ONOFF.put(clientID, 0);  //先置下线
	  LZBusProcessDown lsd=new LZBusProcessDown();
	  lsd.down(clientID);
	}
	
	private  void save2Queue(String clientID,byte[] bs){
		if(bs==null) return ;
		//if(clientID.equals("")) return;
		//if(!clientID.equals("013368260872")) return;
		StringBuffer str= new StringBuffer();
		
		str.append("LZBUS|");
		str.append((new Date()).toLocaleString()+" | 超时"+" | ");
		
		str.append("点名"+" | "+clientID+" | ");
		
		
		
		for(int i=0;i<bs.length;i++){
			str.append(Ecode.DEC2HEX(bs[i], 2));
		}
		str.append("\n");
		Global.logData.add(str.toString());
		//有变化就写日志，性能不高，正式用时删除
		Global.logData.trigger();
	}
	

}
