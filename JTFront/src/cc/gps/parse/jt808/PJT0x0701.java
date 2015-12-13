package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0701 {
	private static final Log log = LogFactory.getLog(PJT0x0701.class);
	
	public Segment<Long> elen; //电子运单长度
	public Segment<String> data; //电子运单内容
	

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0701(ByteBuf bb,int len){
		bb.skipBytes(s);
		elen   =new Segment<Long>(bb,4,new LongParse());
		data  =new Segment<String>(bb,len-4,new StringParse());
	}
	public void convert(){
		System.out.println(
				"电子运单长度           "+elen.value+   
				"\n电子运单内容          "+data.value
				);
	}	
}