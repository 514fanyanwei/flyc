package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class PJT0x0108  {
	private static final Log log = LogFactory.getLog(PJT0x0108.class);
	
	public Segment<Integer> upgradeType; //升级类型
	public Segment<Integer> upgradeResult; //升级结果

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0108(ByteBuf bb){
		bb.skipBytes(s);
		upgradeType   =new Segment<Integer>(bb,1,new IntegerParse());
		upgradeResult    =new Segment<Integer>(bb,1,new IntegerParse());
	}
	public void convert(){
		System.out.println(
				"升级类型           "+upgradeType.value+   
				"\n升级结果          "+upgradeResult.value                    
				);
	}	
}