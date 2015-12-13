package cc.gps.parse.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.buffer.ByteBuf;
import cc.gps.active.lzbus.LZBusProcessReceive;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class LZBus0x75 {
	public Segment<String> clientID; //终端编号
	public Segment<String> simnum;   //phone number
	public Segment<String> ip;   //动态IP
	public Segment<Integer> quality;   //信号质量
	public Segment<String> driverNUM;   //司机工号
	public Segment<String> lineNum;   //线路号
	
	private static final Log log = LogFactory.getLog(LZBus0x75.class);
	private int s=0;
	
	public LZBus0x75(ByteBuf bb){
		bb.skipBytes(s);
		clientID   =new Segment<String>(bb,4,new StringParse());
		simnum   =new Segment<String>(bb,6,new StringParse());
		ip   =new Segment<String>(bb,4,new StringParse());
		quality   =new Segment<Integer>(bb,1,new IntegerParse());
		driverNUM   =new Segment<String>(bb,10,new StringParse());
		lineNum   =new Segment<String>(bb,4,new StringParse());
		
		byte bs[]=Ecode.HexString2ByteArray(lineNum.value);
		StringBuffer sb=new StringBuffer();
		//log.info(lineNum.value+"**************");
		for(int i=0;i<bs.length;i++){
			if(bs[i]<10)
				sb.append("0");
			sb.append(String.valueOf(bs[i]));
		}
		lineNum.value=sb.toString();
		//log.info(lineNum.value+"**************");
	}
	
	public JT0x0102 convert(){
		JT0x0102 td=new JT0x0102();
		
		return td;
	}
	public String toString(){
		return clientID.value+"  "+simnum.value+"  "+ip.value+"  "+quality.value+"  "+driverNUM.value+"  "+lineNum.value;
	}

}
