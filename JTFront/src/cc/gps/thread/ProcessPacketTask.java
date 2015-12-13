package cc.gps.thread;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;
import java.util.Date;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.IProcessReceive;
import cc.gps.config.Global;
import cc.gps.data.SendData;
import cc.gps.util.Ecode;

public class ProcessPacketTask extends Task{

	private static final Log log = LogFactory.getLog(ProcessPacketTask.class);
	private IProcessReceive process;
	private ByteBuf byteBuf;
	private ChannelHandlerContext ctx;
	
	public ProcessPacketTask(IProcessReceive process,ByteBuf byteBuf,ChannelHandlerContext ctx){
		this.process=process;
		this.byteBuf=byteBuf;
		this.ctx=ctx;
	}
	public void run(){
		if(process==null){
			log.debug("前端数据采集服务器在分析蓝斯公交报文开始标识时，没找到相应的处理器");
			return;
		}
		
		//交报文处理程序处理，返回应答终端的报文
		SendData sendData=process.processData(byteBuf,ctx);
		
		if(sendData!=null){
			//待下发的数据
			/*
			log.info("*****由ProcessPacketTask发向终端"+sendData.getClientID()+"的数据是:");
			byte[] bs=sendData.getBytes();
			for(int i=0;i<bs.length;i++){
				System.out.print(Ecode.DEC2HEX(bs[i],2));
			}
			System.out.println();*/
			
			send(ctx,sendData);
		}
	}
	//仅用于回复终端
	private void send(ChannelHandlerContext ctx,SendData sendData) { 
		//发送回复数据
		byte [] bs = sendData.getBytes();
		/*if(log.isDebugEnabled())
		{
			log.debug("\n前端数据采集服务器应答终端的报文是:");
			log.debug(Ecode.ByteArray2HexString(bs, -1, -1));	
		}*/
		
		ByteBuf bb=Unpooled.buffer(bs.length);
		bb.writeBytes(bs);
		ctx.writeAndFlush(bb);
   	}
	@Override
	public Task[] taskCore() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean useDb() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean needExecuteImmediate() {
		// TODO Auto-generated method stub
		return true;  //立即执行
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}
}
