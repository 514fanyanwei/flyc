package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;

public class PJT0x0302 {
	private static final Log log = LogFactory.getLog(PJT0x0302.class);
	
	public Segment<Integer> serialID; //应答流水号 
	public Segment<Integer> answerID; //答案 ID
	

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0302(ByteBuf bb){
		bb.skipBytes(s);
		serialID   =new Segment<Integer>(bb,2,new IntegerParse());
		answerID   =new Segment<Integer>(bb,1,new IntegerParse());
	}
	public void convert(){
		System.out.println(
				"应答流水号           "+serialID.value+
				"答案ID"+answerID.value
				);
	}	
}