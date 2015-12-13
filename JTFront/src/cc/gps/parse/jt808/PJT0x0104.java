package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JT0x0104;
import cc.gps.parse.DateTimeParse;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class PJT0x0104 {
	private static final Log log = LogFactory.getLog(PJT0x0104.class);
	
	public Segment<Integer> answerID; //应答流水号  对应的终端参数查询消息的流水号
	public Segment<Integer> answerNUM; //应答参数个数 
	public HashMap<Long,String> parameters=new HashMap<Long,String>();//参数项列表
	
	public StringBuffer contents;

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0104(ByteBuf bb){
		bb.skipBytes(s);
		contents=new StringBuffer();
		answerID   =new Segment<Integer>(bb,2,new IntegerParse());
		answerNUM    =new Segment<Integer>(bb,1,new IntegerParse());
		
		while(bb.readableBytes()>2){  //>2是因为后面有 校验位1  + 结束符 1
			long pid=bb.readUnsignedInt();
			int plen=bb.readUnsignedByte();
			String value= (new Segment<String>(bb,plen,new StringParse())).value;
			log.info("参数ID"+Ecode.DEC2HEX(pid,4)+"********长度:"+plen);
			parameters.put(pid,value);
			log.info("参数ID"+Ecode.DEC2HEX(pid,4)+"---------值:"+parameters.get(pid));
			//log.info(more.get(eid));
			contents.append(Ecode.DEC2HEX(pid,4));
			contents.append(Ecode.DEC2HEX(plen,2));
			contents.append(value);
		}
	}
	
	public JT0x0104 convert(){
		JT0x0104 jt=new JT0x0104();
		jt.replySerialID=answerID.value;
		jt.counts=answerNUM.value;
		jt.content=contents.toString();
		return jt;
	}
		
}
