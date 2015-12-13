package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0805 {
	private static final Log log = LogFactory.getLog(PJT0x0805.class);
	
	public Segment<Integer> serialID; //应答流水号  
	public Segment<Integer>  result; //结果
	public Segment<Integer>  num;//多媒体 ID 个数 
	public Segment<String> mlist;//多媒体 ID 列表

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0805(ByteBuf bb,int len){
		bb.skipBytes(s);
		serialID   =new Segment<Integer>(bb,2,new IntegerParse());
		result  =new Segment<Integer>(bb,1,new IntegerParse());
		num  =new Segment<Integer>(bb,2,new IntegerParse());
		mlist=new Segment<String>(bb,len-5,new StringParse());
	}
	public void convert(){
		System.out.println(
				"\n应答流水号            "+serialID.value+  
				"\n结果            "+result.value+
				"\n多媒体 ID 个数           "+num.value+
				"\n多媒体 ID 列表           "+mlist.value
				);
	}	
}