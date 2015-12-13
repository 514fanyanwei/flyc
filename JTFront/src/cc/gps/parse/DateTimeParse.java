package cc.gps.parse;

import cc.gps.util.Ecode;
import io.netty.buffer.ByteBuf;
/**
 * 
 * @author Administrator
 * 仅解析YY-MM-DD-hh-mm-ss格式
 */
public class DateTimeParse implements IParse<String> {

	@Override
	public String parse(ByteBuf bb, int start, int len) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parse(ByteBuf bb, int len) throws ParseException {
		if(len<=0) throw new ParseException("解析时要求长度大于0");
		StringBuffer val=new StringBuffer();
		for(int i=0;i<6;i++){
			String temp=Ecode.DEC2HEX(bb.readUnsignedByte(),2);
			val.append(String.valueOf(temp));
			if(i<2) val.append("-");
			if(i==2) val.append(" ");
			if((i>2)&&(i<5)) val.append(":");
			
		}
		
		return val.toString();
	}

}
