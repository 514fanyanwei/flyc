package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0702 {
	private static final Log log = LogFactory.getLog(PJT0x0702.class);
	
	public Segment<Integer> status; //状态
	public Segment<String>  dateTime; //时间
	public Segment<Integer> IC;     //IC 卡读取结果
	public Segment<Integer> nlen; //驾驶员姓名长度
	public Segment<String>  dName; ////驾驶员姓名
	public Segment<String>  licence; //从业资格证编码
	public Segment<Integer>  olen; ////发证机构名称长度
	public Segment<String>  organ; //发证机构名称
	public Segment<String>  validity; ////证件有效期
	
	

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0702(ByteBuf bb){
		bb.skipBytes(s);
		status   =new Segment<Integer>(bb,1,new IntegerParse());
		dateTime  =new Segment<String>(bb,6,new StringParse());
		IC   =new Segment<Integer>(bb,1,new IntegerParse());
		nlen   =new Segment<Integer>(bb,1,new IntegerParse());
		dName  =new Segment<String>(bb,nlen.value,new StringParse());
		licence  =new Segment<String>(bb,20,new StringParse());
		olen   =new Segment<Integer>(bb,1,new IntegerParse());
		organ  =new Segment<String>(bb,olen.value,new StringParse());
		validity  =new Segment<String>(bb,4,new StringParse());
	}
	public void convert(){
		System.out.println(
				"\n状态            "+status.value+  
				"\n时间            "+dateTime.value+
				"\nIC 卡读取结果   "+IC.value+
				"\n驾驶员姓名长度  "+nlen.value+
				"\n驾驶员姓名      "+dName.value+
				"\n从业资格证编码  "+licence.value+
				"\n发证机构名称长度"+olen.value+
				"\n发证机构名称    "+organ.value+
				"\n证件有效期      "+validity

				);
	}	
}