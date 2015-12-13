package cc.net;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.lzbus.LZBusProcessDown;
import cc.gps.config.Global;
import cc.gps.service.SocketSessionManager;
import cc.net.lzbus.GPSProcessVehicleHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

public class IdleHandler extends ChannelDuplexHandler{
	private static final Log log = LogFactory.getLog(IdleHandler.class);
	private ICreatCheckPacket iccp;
	private AttributeKey<String> cidkey;
	public IdleHandler(ICreatCheckPacket iccp,AttributeKey<String> cidkey){
		this.iccp=iccp;
		this.cidkey=cidkey;
	}
 	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
 		String clientID=ctx.channel().attr(cidkey).get();
          if (evt instanceof IdleStateEvent) {
              IdleStateEvent e = (IdleStateEvent) evt;
              if (e.state() == IdleState.READER_IDLE) {
            	  log.info(clientID+"终端在指定时间内无数据传入");
            	  //拟下发检查终端在线包（一般为点名等需终端应答的报文）,检查终端是否在线
            	  int count=ctx.channel().attr(Global.CALLCOUNT).get();
            	  if(count>0){ //下发检查报文后，还未收到终端应答，则关闭连接
            		  iccp.down(clientID);
            	  }else{
            		  iccp.createRollCall(ctx);
            	  }
            	  
              } else if (e.state() == IdleState.WRITER_IDLE) {
            	  log.info("服务器在指定时间内无数据发送");
            	  //下发心跳包告知终端:服务器在线
            	  iccp.createHeartBeat(ctx);
              }
          }
      }
  }
