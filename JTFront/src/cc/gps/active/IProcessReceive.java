package cc.gps.active;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import cc.gps.data.SendData;
import cc.gps.data.jt808.JTReceiveData;

public interface IProcessReceive {

	SendData processData(ByteBuf byteBuf,ChannelHandlerContext ctx);
	/*
	 * 该方法的主要操作流程:
	 * //1   处理接收的消息头
		    List<ReceiveData> datas=processMessageHead(buffer);
		//2 根据messageID选用不同的处理方案  处理消息体
	 */
	
	//以下方法仅供processData()方法调用，不单独使用
	List<JTReceiveData> processMessageHead(ByteBuf byteBuf);  

	byte[] processMessageBody(JTReceiveData data);
	byte[] processMessageExtral(JTReceiveData data);
	void saveSessionCache(String clientID, ChannelHandlerContext ctx, long expiredTime);
	
	//向平台转发数据
	void processTrans(JTReceiveData data);
	
	//向监控转发
	void processMonitor(List<JTReceiveData> datas);
	//写数据库
	public void write2DB(JTReceiveData data);

}
