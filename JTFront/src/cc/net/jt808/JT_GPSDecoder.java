package cc.net.jt808;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.jt808.JTProcess0x0102;
import cc.gps.config.Global;
import cc.gps.util.Ecode;

public class JT_GPSDecoder extends ByteToMessageDecoder { 
	private static final Log log = LogFactory.getLog(JT_GPSDecoder.class);
	  @Override
	  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
		int len=in.readerIndex();
		
		
		ByteBuf bb=in.duplicate();
		bb.skipBytes(len);
		while (bb.isReadable()) {
    		byte b=bb.readByte();
    		System.out.print(Ecode.DEC2HEX(b,2));
    		
    	}System.out.println();
		
		
		//System.out.println("len:"+len+"^^^^");
		//EscapePacket ep=beforeProcess(in.duplicate(),len); //统计转义字符个数
		//EscapePacket ep=beforeProcess(in.copy(len,in.readableBytes())); //统计转义字符个数
		EscapePacket ep=beforeProcess(in,len); //统计转义字符个数
		if(!ep.isAll) return; //不完整报文
		
		int count=ep.count;
		//boolean skip=false;
		//while(in.readableBytes() >= 5)
		//while(ep.bb.readableBytes()>=1)
		{
			/*
			ByteBuf savebb=in.duplicate();
			int i=0;
			savebb.skipBytes(len);
			while(savebb.isReadable())
			{
				System.out.print(Ecode.DEC2HEX(savebb.readUnsignedByte(),2)+"*");
			}*/
			String stemp=Ecode.decToBin(ep.bb.getUnsignedByte(3))+Ecode.decToBin(ep.bb.getUnsignedByte(4));
			len=Ecode.binToDec(stemp.substring(6)); //为从流水号或消息包封装项(若有的话)后到校验前内容去掉转义符的实际真实长度
			//测度用
			log.info("\nJT_GPSDecode:--消息体属性字段:"+stemp+"    报文长："+ len+"");
			int more=Ecode.binToDec(stemp.substring(2,3));
			//len=len+count;//加上转义字符数
			if(more==0){
				len=len+13+2;//5 消息头长,2:校验码+结束符 //in.getUnsignedShort(in.readerIndex()+1)+1+3;
			}else
				len=len+13+4+2;//5 消息头长,2:消息包封装项 2:校验码+结束符 //in.getUnsignedShort(in.readerIndex()+1)+1+3;
			
		    //log.info(len+"***********---------------++++++");
		   /*
			if(ep.bb.readableBytes()<len){
		    	System.out.println("*_____");
		    	//break;
		    }*/
		    ByteBuf frame = ctx.alloc().buffer(ep.bb.readableBytes());
		    //frame.writeBytes(ep.bb,len);
		    frame.writeBytes(ep.bb,len);
		    out.add(frame);
		    in.skipBytes(len+count);
		    //skip=true;
		    /*
		    ByteBuf frame = ctx.alloc().buffer(len);
		    frame.writeBytes(in, in.readerIndex(), len);
		    in.skipBytes(len);
		    out.add(frame); // (4)*/
		}
		//if(skip) in.skipBytes(count);
	  }
	  
	private EscapePacket beforeProcess(ByteBuf buffer,int len){
		int count=0;
		boolean find=false;
		boolean find_start=false;
		
		int k=0;
		byte[] b=new byte[buffer.readableBytes()];
		EscapePacket ep=new EscapePacket();
		for(int i=0;i<b.length;i++){
			byte bit1=buffer.getByte(len+i);
			byte bit2=buffer.getByte(len+i+1);
			//检查是否找到开始标识 0x7e
			if((!find_start)&&(bit1!=0x7e)) {count++;continue; }
			//检查是否是不完整报文的结束符
			if((!find_start)&&(bit1==0x7e)&&(bit2==0x7e)) {count++;continue; }
			b[k++]=bit1;//buffer.getByte(i);
			if((bit1==0x7d)&&(bit2==0x02)){
				b[k-1]=(byte) 0x7e;
				find=true;
			}
			if(!find){
				if((bit1==0x7d)&&(bit2==0x01)){
					b[k-1]=(byte) 0x7d;
					find=true;
				}
			}
			if(find){
				i++; //跳过1个;
				count++;//记录转义个数
				//log.info(count);
				find=false;
			}
			//找到结束标识
			if((find_start)&&(bit1==0x7e)){
				ep.isAll=true;
				break;
			}
			find_start=true;//置找到开始标识，接下来找结束标识
		}
		
		
		ByteBuf bbf=Unpooled.buffer(k);
		
		//log.info("转义后的新报文:");
		for(int i=0;i<k;i++){
			bbf.writeByte(b[i]);
			//测试用
			//System.out.print(Ecode.DEC2HEX(b[i],2)+"|");
		}
		ep.count=count;
		ep.bb=bbf;
		
		
		return ep;
	}
}