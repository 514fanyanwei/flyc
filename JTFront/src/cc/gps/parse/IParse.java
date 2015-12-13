package cc.gps.parse;

import io.netty.buffer.ByteBuf;

public interface IParse<T> {
	//start起始位
	//len 长度
	//dotpos 小数点位置
	public T parse(ByteBuf bb,int start,int len) throws ParseException;
	public T parse(ByteBuf bb,int len) throws ParseException;
}
