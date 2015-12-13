package cc.gps.parse.jt808;

import java.util.HashMap;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0705 {
	private static final Log log = LogFactory.getLog(PJT0x0705.class);
	
	public Segment<Integer> number; //数据项个数 
	public Segment<String>  rtime; //CAN 总线数据接收时间
	//public Segment<Integer> canitem;     //CAN 总线数据项
	public HashMap<String,String> candata=new HashMap<String,String>();//附加信息 
	
	

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0705(ByteBuf bb){
		bb.skipBytes(s);
		number   =new Segment<Integer>(bb,2,new IntegerParse());
		rtime  =new Segment<String>(bb,5,new StringParse());
	}
	public void convert(){
		System.out.println(
				"\n数据项个数            "+number.value+  
				"\nCAN 总线数据接收时间            "+rtime.value
				);
	}	
}