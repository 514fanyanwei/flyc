package cc.net.lzbus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.Attribute;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.IBuildSendPacket;
import cc.gps.active.ProcessSendFactory;
import cc.gps.config.Config;
import cc.gps.config.Global;
import cc.gps.data.X2CData;
import cc.gps.data.jt808.JT0x0002;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.queue.GPSQueue;
import cc.gps.service.BusinessSessionManager;
import cc.gps.service.MonitorSessionManager;
import cc.gps.service.TransSessionManager;
import cc.gps.util.SerializeUtil;
import cc.net.IChannelPipeline;
import cc.net.ICreatCheckPacket;
import cc.net.IdleHandler;
import cc.net.Platform_CreateCheckPacket;


public class LZBUS_Web_IChannelPipeline implements IChannelPipeline{

	@Override
	public void initPipeline(SocketChannel ch) throws Exception {
		ICreatCheckPacket iccp=new Platform_CreateCheckPacket();
		ch.pipeline().addLast(
                new ObjectEncoder(),
                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                new IdleStateHandler(60, 60, 0),//1 读    2 写
          		new IdleHandler(iccp,Global.WEBID),
                new WEBServerHandler());
		/*
		ch.pipeline().addLast("timeout", new IdleStateHandler(120, 60, 0));//读120 写60s
        ICreatCheckPacket iccp=new LZBUS_Web_CreateCheckPacket();
        ch.pipeline().addLast("idleHandler", new IdleHandler(iccp));*/
		
	}
}
class WEBServerHandler  extends ChannelInboundHandlerAdapter {
	private static final Log log = LogFactory.getLog(WEBServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
    	log.info("接收到来自WEB平台的报文");
    	ctx.channel().attr(Global.CALLCOUNT).set(0);
    	/*
		 * 包含两种:
		 * 1 连接请求  messageID 
		 * 2 平台心跳
		 * 3 平台对前端的应答
		 * 4 下发指令,根据终端ID从缓存中找到与终端关联的Session,通过该session下发数据
		 *  
		 * 应含:
		 *   (1)messageID 指令类型
		 *   (2)clientID  终端ID
		 *   (3)content   内容  ,不同指令,该字段的类型不同
		 *   
		 */
		if (obj==null) return;
		Attribute<String> attr = ctx.channel().attr(Global.WEBID);
		X2CData data=(X2CData)SerializeUtil.deserializeObject((byte[])obj);
		
		int messageID=data.getMessageID();
		//int ctid=data.getCtid();
		log.debug("收到Web平台发来的报文ID:"+Integer.toHexString(messageID));
		//连接请求
		if(messageID==0x0102){  //借用JT/T808  表示交换平台与前端的首次连接
			//缓存该session
			//TransSessionManager.putContent("cqckweb",session,Long.parseLong(Config.getKey("clientSessionExpired")));
			attr.set(String.valueOf(data.getPid()));
			String businessID=(String)(data.getContent());
			log.info(data.getPid()+"登陆成功");
			MonitorSessionManager.putContent(String.valueOf(data.getPid()),ctx);
			//首次连接回复一个通用应答 0x0001
			send(ctx);
		}
		else{
			int ctid=data.getCtid();
			
			//交给JBuildSendPacketFactory工厂 负责发送到终端
			IBuildSendPacket jtb=ProcessSendFactory.createProcess(ctid, messageID);
			boolean isSend=jtb.buildSendPacket(data.getClientID(),data.getContent(),data.getSerialID()); //返回下发终端结果
			data.setMilisSendTime(new Date().getTime()); //记录发送时间
			Global.orderSended.put(data.getClientID()+data.getSerialID(),data);
		}
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        
    	log.debug(cause);
    	/*
    	logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.", cause);*/
        ctx.close();
    }
    
	//发给平台一个通用应答，用于首次连接成功后的回应
	private void send(ChannelHandlerContext ctx) { 
		
		JT0x0002 data = new JT0x0002();
		JTMessageHead head = new JTMessageHead();
		head.messageID=0x0002;
		data.head=head;
		data.head.start=0x02;
		log.debug("向"+ctx.channel().remoteAddress()+"发送回应报文--------");
		try {
			ctx.writeAndFlush(SerializeUtil.serializeObject(data));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}
