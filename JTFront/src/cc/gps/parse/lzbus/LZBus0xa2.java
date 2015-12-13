package cc.gps.parse.lzbus;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.jt808.JT0x0801;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class LZBus0xa2 {
	public Segment<String> pid; //照片序号
	
	private static final Log log = LogFactory.getLog(LZBus0xa2.class);
	
	private int s=0;
	private String sim;
	public LZBus0xa2(ByteBuf bb,String sim){
		bb.skipBytes(s);
		this.sim=sim;
		pid     =new Segment<String> (bb,4,new StringParse());
	}
	/*
	public LZBusImage convert(){
		LZBusImage lzImage=Global.CID2BodyArray.get(sim);
		return lzImage;
	}*/
}
