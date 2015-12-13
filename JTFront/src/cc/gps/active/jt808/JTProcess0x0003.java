package cc.gps.active.jt808;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x9999;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.jt808.PJT0x0301;
import cc.gps.service.SocketSessionManager;
import cc.gps.util.Cache;
import cc.gps.util.Ecode;

public class JTProcess0x0003 extends JTProcessReceive{
private static final Log log = LogFactory.getLog(JTProcess0x0301.class);
	//向平台传递终端下线
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled()){
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0003")+":-------------");
		
			ByteBuf bff=inData.bb.duplicate();
			while(bff.isReadable()){
				System.out.print(Ecode.DEC2HEX(bff.readUnsignedByte()));
			}System.out.println();
		
		}
		
		
		down(inData.head.phone);
		
		//向终端回送1个平台通用应答
		/*
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		return bs;*/
		return null;
		
	}
	
	public void down(String clientID){
		if(clientID==null) return;
    	
    	//置车辆下线状态
		log.debug("向平台发送信息："+clientID+"终端下线");
		
		JT0x9999 td = new JT0x9999();
		//以下五项必须(前四项通用)
		td.head.phone=clientID; //必须有
		td.head.start=0x7e;//必须有
		td.head.messageID=0x9999;
		//关闭及清除,终端连接
		Cache cache=(Cache)SocketSessionManager.getContent(clientID);
		if(cache!=null){
			ChannelHandlerContext ctx=(ChannelHandlerContext)(cache.getValue());
			ctx.close();
		}
		SocketSessionManager.invalidate(clientID);
		Global.CID2ONOFF.put(clientID, 0);  //先置下线
		processTrans(td);  //调用父类方法统一转发
		write2DB(td);
		//save2Queue(clientID);
	}
	private  void save2Queue(String clientID){
		StringBuffer str= new StringBuffer();
		
		str.append("JTBUS|");
		str.append((new Date()).toLocaleString()+" | An existing connection was closed by the remote host"+" | ");
		
		str.append("down"+" | "+clientID+" | ");
		
		
		str.append("\n");
		Global.logData.add(str.toString());
		//有变化就写日志，性能不高，正式用时删除
		Global.logData.trigger();
	}
}