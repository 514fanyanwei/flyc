package cc.gps.parse.lzbus;

import io.netty.buffer.ByteBuf;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;

public class LZBus0x71 {
	public Segment<Integer> ptSerialID; //应答命令序号
	public Segment<Integer> ptMessageID; //中心命令ID
	public Segment<Integer> result; //处理结果
	private int s=0;
	private int messageID;
	public LZBus0x71(ByteBuf bb,int messageID){
		this.messageID=messageID;
		bb.skipBytes(s);
		ptSerialID   =new Segment<Integer>(bb,2,new IntegerParse());
		ptMessageID  =new Segment<Integer>(bb,1,new IntegerParse());
		result       =new Segment<Integer>(bb,1,new IntegerParse());
	}
	public C2XAnswer convert(){
		C2XAnswer td = new C2XAnswer();
		
		td.replySerialID=ptSerialID.value;
		td.messageID=ptMessageID.value;
		td.result=result.value;
		
		return td;
		
	}
}
