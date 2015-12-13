package cc.net.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.active.IProcessReceive;
import cc.gps.active.lzbus.LZBusProcessDown;
import cc.gps.active.lzbus.LZBusProcessReceive;
import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.service.SocketSessionManager;
import cc.gps.thread.ProcessPacketTask;
import cc.gps.thread.Task;
import cc.gps.util.Ecode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class GPSProcessVehicleHandler extends ChannelInboundHandlerAdapter {

	private static final Log log = LogFactory.getLog(GPSProcessVehicleHandler.class);
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) {
        //ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	ctx.channel().attr(Global.CALLCOUNT).set(0);
    	IProcessReceive process=new LZBusProcessReceive();
    	
    	ByteBuf in = (ByteBuf) msg;
    	
    	ByteBuf indup=in.duplicate();
    	while (indup.isReadable()) {
    		byte b=indup.readByte();
    		System.out.print(Ecode.DEC2HEX(b,2));
    	}
    	System.out.println();
    	
    	
    	Task task = new ProcessPacketTask(process,in,ctx);
    	Global.tp.addTask(task);
    	ByteBuf in2=in.duplicate();
    	String mid=Ecode.DEC2HEX(in2.getUnsignedByte(6),2);
    	String order=Recourse.getOrder("lzbus00"+mid);
    	
    	//CCGPSF.jTextArea1.insert(new Date().toLocaleString()+" | "+ctx.channel().attr(Global.CLIENTID).get()+" | "+mid+" | "+order+"\n",0);
		
    	//in.release();
    	/*
    	indup.writeBytes(bs);
    	while (indup.isReadable()) {
    		byte b=indup.readByte();
    		System.out.print(Ecode.DEC2HEX(b,2));
    		System.out.flush();
    	}*/
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	String clientID=ctx.channel().attr(Global.CLIENTID).get();
    	SocketSessionManager.invalidate(clientID);
    	//置车辆下线状态
		log.info(clientID+"终端下线");
		Global.CID2ONOFF.put(clientID, 0);  //先置下线
		LZBusProcessDown lzp=new LZBusProcessDown();
		lzp.down(clientID);
		log.warn("Unexpected exception from downstream.", cause);
        ctx.close();
    }
    
}
