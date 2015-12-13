package cc.gps.active.lzbus;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.jt808.JT0x9999;
import cc.gps.service.SocketSessionManager;
import cc.gps.util.Cache;

public class LZBusProcessDown extends LZBusProcessReceive{
	private static final Log log = LogFactory.getLog(LZBusProcessDown.class);
	
	public void down(String clientID){
		JT0x9999 td = new JT0x9999();
		//以下五项必须(前四项通用)
		td.head.phone=clientID; //必须有
		td.head.start=0x02;//必须有
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
		save2Queue(clientID);
	}
	private  void save2Queue(String clientID){
		StringBuffer str= new StringBuffer();
		
		str.append("LZBUS|");
		str.append((new Date()).toLocaleString()+" | 远程主机强迫关闭了一个现有的连接"+" | ");
		
		str.append("下线"+" | "+clientID+" | ");
		
		
		str.append("\n");
		Global.logData.add(str.toString());
		//有变化就写日志，性能不高，正式用时删除
		Global.logData.trigger();
	}
}
