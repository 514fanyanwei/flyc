package cc.gps.parse.lztaxi;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class LZTAXI0x53  {
	public Segment<Integer> ptMessageID; //中心命令ID
	public Segment<Integer> ptSerialID; //应答命令序号
	
	private int s=0;
	
	public LZTAXI0x53(ByteBuf bb){
		bb.skipBytes(s);
		ptMessageID  =new Segment<Integer>(bb,2,new IntegerParse());
		ptSerialID   =new Segment<Integer>(bb,2,new IntegerParse());
		
	}
	public C2XAnswer convert(){
		C2XAnswer td = new C2XAnswer();
		td.replySerialID=ptSerialID.value;
		td.messageID=ptMessageID.value;
		td.result=0;  //成功
		return td;
		
	}
}
