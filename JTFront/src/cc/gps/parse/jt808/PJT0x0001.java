package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class PJT0x0001 {
	public Segment<Long> ptSerialID; //对应的平台消息的流水号
	public Segment<Long> ptMID; //对应的平台消息的 ID
	public Segment<Long> result; //结果  0：成功/确认；1：失败；2：消息有误；3：不支持
	
	private int s=0; //开始位置  //测试时改为13
	
	
	public PJT0x0001(ByteBuf bb){
		bb.skipBytes(s);
		ptSerialID =new Segment<Long>(bb,2,new LongParse());
		ptMID	   =new Segment<Long>(bb,2,new LongParse());
		result	   =new Segment<Long>(bb,1,new LongParse());
		
	}
	
	public C2XAnswer convert(){
		C2XAnswer jt=new C2XAnswer();
		jt.messageID=ptMID.value.intValue();
		jt.replySerialID=ptSerialID.value.intValue();
		jt.result=result.value.intValue();
		return jt;
	}
}
