package cc.gps.parse.lzbus;

import io.netty.buffer.ByteBuf;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.lzbus.LZBusDriverRequest;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class LZBus0x72 {
	public Segment<String> msg; //信息内容
	private int s=0;
	public LZBus0x72(ByteBuf bb,int len){
		bb.skipBytes(s);
		msg   =new Segment<String>(bb,len,new StringParse());
		
	}
	public LZBusDriverRequest convert(){
		LZBusDriverRequest td = new LZBusDriverRequest();
		td.requestType=Ecode.A2S(msg.value);
		td.pos=null;
		return td;
		
	}
}