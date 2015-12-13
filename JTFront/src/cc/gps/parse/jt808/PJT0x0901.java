package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0901 {
	private static final Log log = LogFactory.getLog(PJT0x0901.class);
	
	public Segment<Long> ziplen; //压缩消息长度  
	public Segment<String>  zipbody; //压缩消息体
	

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0901(ByteBuf bb,int len){
		bb.skipBytes(s);
		ziplen   =new Segment<Long>(bb,4,new LongParse());
		zipbody  =new Segment<String>(bb,len-4,new StringParse());
	}
	public void convert(){
		System.out.println(
				"\n压缩消息长度            "+ziplen.value+  
				"\n压缩消息体            "+zipbody.value
				);
	}	
}