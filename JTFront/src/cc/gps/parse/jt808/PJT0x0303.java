package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;

public class PJT0x0303 {
	private static final Log log = LogFactory.getLog(PJT0x0303.class);
	
	public Segment<Integer> infoType; //信息类型
	public Segment<Integer> flag; //点播/取消标志
	

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0303(ByteBuf bb){
		bb.skipBytes(s);
		infoType   =new Segment<Integer>(bb,1,new IntegerParse());
		flag   =new Segment<Integer>(bb,1,new IntegerParse());
	}
	public void convert(){
		System.out.println(
				"信息类型           "+infoType.value+
				"点播取消标志"+flag.value
				);
	}	
}
