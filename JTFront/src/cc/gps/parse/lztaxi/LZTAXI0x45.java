package cc.gps.parse.lztaxi;

import io.netty.buffer.ByteBuf;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;

public class LZTAXI0x45 {
	public Segment<Integer> ptMessageID; //中心命令ID
	public Segment<Integer> ptSerialID; //应答命令序号
	
	private int s=0;
	
	public LZTAXI0x45(ByteBuf bb){
		bb.skipBytes(s);
		ptMessageID  =new Segment<Integer>(bb,2,new IntegerParse());
		ptSerialID   =new Segment<Integer>(bb,2,new IntegerParse());
		
	}
	public C2XAnswer convert(){
		C2XAnswer td = new C2XAnswer();
		td.replySerialID=ptSerialID.value;
		td.messageID=ptMessageID.value;
		td.result=1;  //执行失败
		return td;
		
	}
}