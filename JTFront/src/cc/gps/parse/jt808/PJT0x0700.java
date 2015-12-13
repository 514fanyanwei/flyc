package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JT0x0700;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0700 {
	private static final Log log = LogFactory.getLog(PJT0x0700.class);
	
	public Segment<Integer> serialID; //应答流水号
	public Segment<Integer> orderID; //命令字
	public Segment<String>  data;//数据块

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0700(ByteBuf bb,int len){
		bb.skipBytes(s);
		serialID   =new Segment<Integer>(bb,2,new IntegerParse());
		orderID    =new Segment<Integer>(bb,1,new IntegerParse());
		data       =new Segment<String>(bb,len-3,new StringParse());
	}
	public JT0x0700 convert(){
		JT0x0700 jt=new JT0x0700();
		jt.replySerialID=serialID.value;
		jt.order=orderID.value;
		jt.content=data.value;
		jt.messageID=0x8700;
		log.info("行驶记录数据上传："+
				"应答流水号           "+serialID.value+   
				"\n命令字          "+orderID.value+
				"\n数据块   "+data.value
				);
		return jt;
	}	
}