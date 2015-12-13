package cc.gps.parse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.buffer.ByteBuf;
import cc.gps.util.Ecode;

public class LongParse implements IParse<Long> {
	private static final Log log = LogFactory.getLog(LongParse.class);
	@Override
	public Long parse(ByteBuf bb, int start, int len) throws ParseException {
		if(len<=0) throw new ParseException("整型长度在解析时要求大于0");
		long val=0;
		/*
		ByteBuf bf=bb.duplicate();
		while (bf.isReadable()) {
    		int b=bf.readUnsignedByte();
    		log.info(Ecode.DEC2HEX(b,2)+"|");
    		System.out.flush();
    	}log.info();
    	*/
		log.info(len+"-----------");
		switch(len){
			case 1:val=(int) bb.getUnsignedByte(start); break;
			case 2:val=(int) bb.getUnsignedShort(start);break;
			case 4:val=(int) bb.getUnsignedInt(start);  break;
			default:{ bb.skipBytes(len);}
		}
		return val;
	}

	@Override
	public Long parse(ByteBuf bb, int len) throws ParseException {
		if(len<=0) throw new ParseException("解析时要求长度大于0");
		long val=0;
		
		ByteBuf bf=bb.duplicate();
		/*
		int k=0;
		while (k<len) {
    		int b=bf.readUnsignedByte();
    		k++;
    		log.info(Ecode.DEC2HEX(b,2)+"|");
    		System.out.flush();
    	}log.info();*/
		switch(len){
			case 1:val=(int) bb.readUnsignedByte(); break;
			case 2:val=(int) bb.readUnsignedShort();break;
			case 4:val=(int) bb.readUnsignedInt();  break;
			default:bb.skipBytes(len);
		}
		return val;
	}
	

}