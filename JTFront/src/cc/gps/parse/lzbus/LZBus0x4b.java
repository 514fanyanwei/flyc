package cc.gps.parse.lzbus;

import io.netty.buffer.ByteBuf;
import cc.gps.data.lzbus.LZBusDriverRequest;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class LZBus0x4b {//必须先解析LZBus0x41
	public Segment<Integer> before;//上一站点编号 
	public Segment<Integer> ddbh;//非营运类型编号
	public Segment<Integer> ddlc; //调度行驶里程
	public LZBus0x4b(ByteBuf bb) {
		before=new Segment<Integer>(bb,2,new IntegerParse());
		bb.skipBytes(3);
		ddbh    =new Segment<Integer>(bb,4,new IntegerParse());
		ddlc    =new Segment<Integer>(bb,4,new IntegerParse());
	}
	
	public LZBusDriverRequest convert(){
		LZBusDriverRequest request=new LZBusDriverRequest();
		request.stationID=before.value;
		request.id=ddbh.value;
		request.fmile=ddlc.value/1000.0;
		//request.sInterval=
		return request;
	}
}
