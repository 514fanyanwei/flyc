package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JT0x0800;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class PJT0x0800 {
	private static final Log log = LogFactory.getLog(PJT0x0800.class);
	
	public Segment<Long> mid; //多媒体数据ID
	public Segment<Integer> mtype; //多媒体类型
	public Segment<Integer> mecode; //多媒体格式编码
	public Segment<Integer> eventID; //事件项编码
	public Segment<Integer> channelID; //通道ID
	
	private int s=0; //开始位置  //测试时改为13
	public PJT0x0800(ByteBuf bb){
		bb.skipBytes(s);
		mid     =new Segment<Long>(bb,4,new LongParse());
		mtype   =new Segment<Integer>(bb,1,new IntegerParse());
		mecode   =new Segment<Integer>(bb,1,new IntegerParse());
		eventID=new Segment<Integer>(bb,1,new IntegerParse());
		channelID    =new Segment<Integer>(bb,1,new IntegerParse());
	}
	
	public JT0x0800 convert(){
		JT0x0800 jt=new JT0x0800();
		jt.mid=mid.value;
		jt.mtype=mtype.value;
		jt.mecode=mecode.value;
		jt.eventID=eventID.value;
		jt.channelID=channelID.value;
		return jt;
	}
		
}
