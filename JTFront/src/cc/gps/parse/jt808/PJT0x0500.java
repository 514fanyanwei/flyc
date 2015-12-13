package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;

public class PJT0x0500 { // 0x0200
	private static final Log log = LogFactory.getLog(PJT0x0500.class);
	
	public Segment<Integer> serialID; //应答流水号
	public PJT0x0200 jt0x0200;
	

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0500(ByteBuf bb){
		bb.skipBytes(s);
		serialID   =new Segment<Integer>(bb,2,new IntegerParse());
		jt0x0200=new PJT0x0200(bb,true); //???
	}
	public void convert(){
		System.out.println(
				"应答流水号           "+serialID.value+
				"0x0200 待检查 修改"
				);
	}	
}