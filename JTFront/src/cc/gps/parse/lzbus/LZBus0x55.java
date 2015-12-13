package cc.gps.parse.lzbus;

import io.netty.buffer.ByteBuf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;


public class LZBus0x55 {
	private static final Log log = LogFactory.getLog(LZBus0x55.class);
	public Segment<String> answerID; //应答命令序号
	public Segment<String> orderID; //中心命令ID 
	public Segment<String> pathID; //限速路段编号 1 
	
	private int s=0; //开始位置  //测试时改为6
	
	
	public LZBus0x55(ByteBuf bb,int len){
		bb.skipBytes(s);
		answerID   =new Segment<String>(bb,2,new StringParse());
		orderID   =new Segment<String>(bb,1,new StringParse());
		pathID = new Segment<String>(bb,len-3,new StringParse());
		log.info("限速路段0x55 长度"+len);
		log.info(answerID.value+"|"+orderID.value+"|"+pathID.value);
		//answerID   =new Segment<String>(bb,2,new StringParse());
		//before     =new Segment<Integer>(bb,2,new IntegerParse());
	}
}