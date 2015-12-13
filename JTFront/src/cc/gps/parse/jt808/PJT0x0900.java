package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0900  {
	private static final Log log = LogFactory.getLog(PJT0x0900.class);
	
	public Segment<Integer> type; //透传消息类型  
	public Segment<String>  content; //透传消息内容
	

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0900(ByteBuf bb,int len){
		bb.skipBytes(s);
		type   =new Segment<Integer>(bb,1,new IntegerParse());
		content  =new Segment<String>(bb,len-1,new StringParse());
	}
	public void convert(){
		System.out.println(
				"\n透传消息类型            "+type.value+  
				"\n透传消息内容            "+content.value
				);
	}	
}