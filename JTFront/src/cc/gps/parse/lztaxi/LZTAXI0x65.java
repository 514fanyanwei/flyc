package cc.gps.parse.lztaxi;

import io.netty.buffer.ByteBuf;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class LZTAXI0x65  {
	public Segment<Integer> ptMessageID; //中心命令ID
	public Segment<Integer> ptSerialID; //应答命令序号
	public Segment<String>  content; //内容
	
	private int s=0;
	
	public LZTAXI0x65(ByteBuf bb,int len){
		bb.skipBytes(s);
		ptMessageID  =new Segment<Integer>(bb,2,new IntegerParse());
		ptSerialID   =new Segment<Integer>(bb,2,new IntegerParse());
		//content      =new Segment<String>(bb,len-5,new StringParse());  //?????  len-4??
		
	}
	public C2XAnswer convert(){
		C2XAnswer td = new C2XAnswer();
		td.replySerialID=ptSerialID.value;
		td.messageID=ptMessageID.value;
		td.result=0;
		return td;
	}
}
