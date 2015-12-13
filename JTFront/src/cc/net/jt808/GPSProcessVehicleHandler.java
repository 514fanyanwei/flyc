package cc.net.jt808;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.active.IProcessReceive;
import cc.gps.active.jt808.JTProcess0x0003;
import cc.gps.active.jt808.JTProcessReceive;
import cc.gps.active.lzbus.LZBusProcessDown;
import cc.gps.active.lzbus.LZBusProcessReceive;
import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.service.SocketSessionManager;
import cc.gps.thread.ProcessPacketTask;
import cc.gps.thread.Task;
import cc.gps.util.Cache;
import cc.gps.util.Ecode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;


public class GPSProcessVehicleHandler extends ChannelInboundHandlerAdapter {

	private static final Log log = LogFactory.getLog(GPSProcessVehicleHandler.class);
	public static int count=0;
	@Override
    public void channelActive(ChannelHandlerContext ctx) {
        //ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	ByteBuf in = (ByteBuf) msg;
    	ctx.channel().attr(Global.CALLCOUNT).set(0);  //记录主动下发点名次数
    	//显示  调试用
    	
    	if(log.isDebugEnabled()){
	    	ByteBuf indup=in.duplicate();
	    	while (indup.isReadable()) {
	    		byte b=indup.readByte();
	    		System.out.print(Ecode.DEC2HEX(b,2));
	    	}
	    	System.out.println();
    	}
    	

    	IProcessReceive process=new JTProcessReceive();
    	Task task = new ProcessPacketTask(process,in,ctx);
    	Global.pool.execute(task);
    	log.debug("cc.net.jt808.GPSProcessVehicleHandler 第"+(count++)+"任务已提交**********");
    	//ByteBuf indup=Unpooled.buffer(in.capacity());
    	//indup.writeBytes(bs);
    	//CCGPSF.jTextArea1.insert(new Date().toLocaleString()+" | "+ctx.channel().attr(Global.CLIENTID).get()+" | "+mid+" | "+order+"\n",0);
		
    	//in.release();
    	
    	//indup.writeBytes(bs);
    	
    	
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	Attribute<String> attr = ctx.channel().attr(Global.CLIENTID);
    	String clientID=attr.get();
    	if(clientID==null) return;
    	/*
    	SocketSessionManager.invalidate(clientID);
    	Global.CID2ONOFF.put(clientID, 0);  //先置下线
    	*/
    	//车辆下线状态
		log.info("TCP链路断开,"+clientID+"终端下线");
		JTProcess0x0003 jtp= new JTProcess0x0003();
		jtp.down(clientID);
		log.debug("Unexpected exception from downstream.", cause);
        //ctx.close();
    }
   
}
