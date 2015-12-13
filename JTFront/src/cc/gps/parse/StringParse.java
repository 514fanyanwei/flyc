package cc.gps.parse;

import cc.gps.util.Ecode;
import io.netty.buffer.ByteBuf;

public class StringParse implements IParse<String> {

	@Override
	public String parse(ByteBuf bb, int start, int len) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parse(ByteBuf bb, int len) throws ParseException {
		if(len<=0) return "";
		//if(len<=0) throw new ParseException("解析时要求长度大于0");
		StringBuffer val=new StringBuffer();
		//for(int i=0;i<len;i++)
		int i=0;
		while(i<len&&bb.isReadable()){
			val.append(Ecode.DEC2HEX(bb.readUnsignedByte(),2));
			i++;
		}
		return val.toString();
	}

}
