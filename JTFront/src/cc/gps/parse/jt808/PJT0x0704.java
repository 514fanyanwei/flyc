package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0704 {
	private static final Log log = LogFactory.getLog(PJT0x0705.class);
	
	public Segment<Integer> number; //数据项个数 
	public Segment<Integer>  postype; //位置数据类型
	public Segment<Integer> poslen;     //位置汇报数据体长度
	public Segment<String> body;//位置汇报数据项 
	
	

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0704(ByteBuf bb){
		bb.skipBytes(s);
		number   =new Segment<Integer>(bb,2,new IntegerParse());
		postype  =new Segment<Integer>(bb,1,new IntegerParse());
		poslen  =new Segment<Integer>(bb,2,new IntegerParse());
		body = new Segment<String>(bb,poslen.value,new StringParse());
	}
	public void convert(){
		System.out.println(
				"\n数据项个数            "+number.value+  
				"\n位置数据类型            "+postype.value+
				"\n位置汇报数据长度          "+poslen.value+
				"\n位置汇报数据项            "+body.value
				);
	}	
}