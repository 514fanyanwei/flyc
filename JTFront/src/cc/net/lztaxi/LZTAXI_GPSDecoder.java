package cc.net.lztaxi;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import cc.gps.util.Ecode;

public class LZTAXI_GPSDecoder extends ByteToMessageDecoder { 
	  @Override
	  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
  
		if (in.readableBytes() < 3) {
	      return; // (3)
	    }
		/*
	    ByteBuf in2=in.duplicate();
	    while(in2.isReadable()){
	    	log.info(Ecode.DEC2HEX(in2.readUnsignedByte(),2));
	    }
	    log.info();
	    */
		int h=in.getUnsignedByte(in.readerIndex()+1);
		int l=in.getUnsignedByte(in.readerIndex()+2);
		String lh=Ecode.DEC2HEX(l,2)+Ecode.DEC2HEX(h,2);
		int len=Integer.parseInt(Ecode.HEX2DEC(lh));
		len=len+4;
	    //int len=in.getUnsignedShort(in.readerIndex()+1)+1+3;
	    
		//log.info(len);
	    //log.info(len+"***********");
	    if(in.readableBytes()<len){
	    	return;
	    }
	    ByteBuf frame = ctx.alloc().buffer(len);
	
        frame.writeBytes(in, in.readerIndex(), len);
        in.skipBytes(len);
	    out.add(frame); // (4)
	   
	  }

	
}