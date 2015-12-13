package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;

public class PJT0x0301 {
	private static final Log log = LogFactory.getLog(PJT0x0301.class);
	
	public Segment<Integer> eventID; //事件 ID 
	

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0301(ByteBuf bb){
		bb.skipBytes(s);
		eventID   =new Segment<Integer>(bb,1,new IntegerParse());
	}
	public void convert(){
		System.out.println(
				"事件 ID           "+eventID.value   
				);
	}	
}