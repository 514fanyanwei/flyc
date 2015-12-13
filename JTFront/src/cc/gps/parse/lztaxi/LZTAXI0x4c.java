package cc.gps.parse.lztaxi;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JT0x0102;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.parse.lzbus.LZBus0x75;
import cc.gps.util.Ecode;

public class LZTAXI0x4c {
	public Segment<String> clientID; //终端编号
	public Segment<String> simnum;   //phone number
	public Segment<String> ip;   //动态IP
	public Segment<Integer> quality;   //信号质量
	public Segment<String> driverNUM;   //司机工号
	public Segment<String> led;   //LED版本号
	
	private static final Log log = LogFactory.getLog(LZTAXI0x4c.class);
	private int s=0;
	
	public LZTAXI0x4c(ByteBuf bb){
		bb.skipBytes(s);
		clientID   =new Segment<String>(bb,4,new StringParse());
		simnum   =new Segment<String>(bb,6,new StringParse());
		ip   =new Segment<String>(bb,4,new StringParse());
		quality   =new Segment<Integer>(bb,1,new IntegerParse());
		driverNUM   =new Segment<String>(bb,4,new StringParse());
		led   =new Segment<String>(bb,4,new StringParse());
		
		byte bs[]=Ecode.HexString2ByteArray(led.value);
		StringBuffer sb=new StringBuffer();
		//log.info(lineNum.value+"**************");
		for(int i=0;i<bs.length;i++){
			if(bs[i]<10)
				sb.append("0");
			sb.append(String.valueOf(bs[i]));
		}
		led.value=sb.toString();
		//log.info(lineNum.value+"**************");
	}
	
	public JT0x0102 convert(){
		JT0x0102 td=new JT0x0102();
		//log.info(this);
		return td;
	}
	public String toString(){
		return "终端ID:"+clientID.value+" simnum: "+simnum.value+" ip: "+ip.value+"  "+quality.value+"  "+driverNUM.value+"  "+led.value;
	}

}
