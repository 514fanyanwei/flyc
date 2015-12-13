package cc.gps.parse.lzbus;

import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusDriverRequest;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;
import io.netty.buffer.ByteBuf;

public class LZBus0x4a {  //必须先解析LZBus0x41
	
	public Segment<Integer> before;//上一站点编号 
	public Segment<String> msg; //
	    
	
	public LZBus0x4a(ByteBuf bb,int len) {
		before=new Segment<Integer>(bb,2,new IntegerParse());
		bb.skipBytes(3);
		msg    =new Segment<String>(bb,len,new StringParse());
		/*
		String s=msg.value;
		//log.info(s+"***"+s.substring(s.length()-2));
		while(s.substring(s.length()-2).equals("03")){
			s=s.substring(0,s.length()-2);
		}*/
		msg.value=Ecode.A2S(msg.value);
	}
	
	public LZBusDriverRequest convert(){
		LZBusDriverRequest request=new LZBusDriverRequest();
		request.requestType=msg.value;
		request.stationID=before.value;
		//request.sInterval=
		return request;
	}

}
