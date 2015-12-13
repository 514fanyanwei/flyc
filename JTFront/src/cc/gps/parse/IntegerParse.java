package cc.gps.parse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.jt808.PJT0x0200;
import cc.gps.util.Ecode;
import io.netty.buffer.ByteBuf;

public class IntegerParse implements IParse<Integer> {
	private static final Log log = LogFactory.getLog(IntegerParse.class);
	@Override
	public Integer parse(ByteBuf bb, int start, int len) throws ParseException {
		if(len<=0) throw new ParseException("整型长度在解析时要求大于0");
		int val=0;
		/*
		ByteBuf bf=bb.duplicate();
		while (bf.isReadable()) {
    		int b=bf.readUnsignedByte();
    		log.info(Ecode.DEC2HEX(b,2)+"|");
    		System.out.flush();
    	}*/
    	
		switch(len){
			case 1:val=(int) bb.getUnsignedByte(start); break;
			case 2:val=(int) bb.getUnsignedShort(start);break;
			case 4:val=(int) bb.getUnsignedInt(start);  break;
		}
		return val;
	}

	@Override
	public Integer parse(ByteBuf bb, int len) throws ParseException {
		if(len<=0) throw new ParseException("解析时要求长度大于0");
		int val=0;
		/*
		ByteBuf bf=bb.duplicate();
		while (bf.isReadable()) {
    		int b=bf.readUnsignedByte();
    		log.info(Ecode.DEC2HEX(b,2)+"|");
    		System.out.flush();
    	}log.info();*/
		switch(len){
			case 1:val=(int) bb.readUnsignedByte(); break;
			case 2:val=(int) bb.readUnsignedShort();break;
			case 4:val=(int) bb.readUnsignedInt();  break;
		}
		return val;
	}
	

}
