package cc.net.jt808;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.jt808.JTBuildSendPacket;
import cc.gps.active.jt808.JTBuildSendPacketFactory;
import cc.gps.active.jt808.JTProcess0x0003;
import cc.gps.config.Global;
import cc.gps.service.SocketSessionManager;
import cc.gps.util.Cache;
import cc.gps.util.Ecode;
import cc.net.ICreatCheckPacket;

public class JT808_Front_CreateCheckPacket  implements ICreatCheckPacket{
	private static final Log log = LogFactory.getLog(JT808_Front_CreateCheckPacket.class);
	
	@Override
	public void createHeartBeat(ChannelHandlerContext ctx) { //平台心跳
		log.debug("拟发送平台心跳*********");
		/*
		byte[] bs=null;
		
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x0002); //心跳包
		bs=jtb.buildSendPacket(null); 
		ByteBuf bb=Unpooled.buffer(bs.length);
		bb.writeBytes(bs);
		ctx.writeAndFlush(bb);*/
		
	}

	 //在规定时间没收到终端上传报文，点名
	@Override
	public void createRollCall(ChannelHandlerContext ctx) {
		ctx.channel().attr(Global.CALLCOUNT).set(1);  //设置下发1次
		/*
		Attribute<String> attr = ctx.channel().attr(Global.CLIENTID);
		//写数据表GPSPacketLog,保存收到的原始报文
		String clientID=attr.get();
		down(clientID);*/
	}

	
	@Override
	public void down(String clientID) {
		//置车辆下线状态
	  log.debug("置 "+clientID+" 终端下线");
	  //通知平台，终端已下线
	  JTProcess0x0003 jtp= new JTProcess0x0003();
	  jtp.down(clientID);
	  /*
	  Cache cache=(Cache)SocketSessionManager.getContent(clientID);
	  if(cache!=null){
			ChannelHandlerContext ctx=(ChannelHandlerContext)(cache.getValue());
			ctx.close();
	  }
	  
	  SocketSessionManager.invalidate(clientID);
	  Global.CID2ONOFF.put(clientID, 0);  //先置下线*/
	  
	  
	}
	
	/*private  void save2Queue(String clientID,byte[] bs){
		if(bs==null) return ;
		//if(clientID.equals("")) return;
		//if(!clientID.equals("013368260872")) return;
		StringBuffer str= new StringBuffer();
		
		str.append("JT808|");
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
*/	

}