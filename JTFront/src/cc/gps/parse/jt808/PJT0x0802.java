package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0802 {
	private static final Log log = LogFactory.getLog(PJT0x0802.class);
	
	public Segment<Integer> serialID; //应答流水号  
	public Segment<Integer>  total; //多媒体数据总项数
	public Segment<Long> mid;//多媒体ID
	public Segment<Integer> mtype;//多媒体类型
	public Segment<Integer> cid;//通道ID
	public Segment<Integer> eventID;//事件项编码
	
	public Segment<String>  indexItem;//检索项 
	PJT0x0200 pjt0200;

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0802(ByteBuf bb,int len){
		bb.skipBytes(s);
		serialID   =new Segment<Integer>(bb,2,new IntegerParse());
		total  =new Segment<Integer>(bb,2,new IntegerParse());
		mid=new Segment<Long>(bb,4,new LongParse());
		mtype=new Segment<Integer>(bb,1,new IntegerParse());
		cid=new Segment<Integer>(bb,1,new IntegerParse());
		eventID=new Segment<Integer>(bb,1,new IntegerParse());
		indexItem=new Segment<String>(bb.duplicate(),len-11,new StringParse());
		pjt0200=new PJT0x0200(bb,true);
	}
	public void convert(){
		System.out.println(
				"\n应答流水号            "+serialID.value+  
				"\n多媒体数据总项数            "+total.value+
				"\n多媒体ID"+mid.value+
				"\n多媒体类型"+mtype.value+
				"\n通道ID"+cid.value+
				"\n事件项编码"+eventID.value+
				"\n检索项            "+indexItem.value+
				" pjt0200:"+pjt0200.convert()
				);
	}	
}