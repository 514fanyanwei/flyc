package cc.gps.parse.lzbus;

import io.netty.buffer.ByteBuf;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;

public class LZbusHead {
	public Segment<Integer> stx; //帧头部(STX)
	public Segment<Integer> len; //帧长度(LEN)
	private int s=0;
	
	public LZbusHead(ByteBuf bb){
		bb.skipBytes(s);
		stx   =new Segment<Integer>(bb,1,new IntegerParse());
		len   =new Segment<Integer>(bb,1,new IntegerParse());
	}
}
