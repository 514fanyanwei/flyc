package cc.gps.parse.lztaxi;

import java.util.Iterator;
import java.util.LinkedList;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.parse.BCDParse;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.parse.lzbus.LZBus0x41;
import cc.gps.util.Ecode;
import cc.gps.util.FileUtil;

public class LZTAXI0x7e {  //青岛恒星计价器
	//skip(2)
	public Segment<Integer> fid; //功能字    	0x02-单笔运营数据
	public Segment<String> driverNO; //司机代码 4 BCD码  
	public Segment<String> vehicleNO; //车号	6	ASCII码
	public Segment<String> offMiles; //空车行驶里程	2	单位0.1公里 BCD码(01H 25H==>12.5公里)
	public Segment<String> onDateTime;//上车日期时间	6	包含年月日时分的BCD码
	public Segment<String> offDateTime;//下车时间	2	包含时分的BCD码
	public Segment<String> unitPrice  ;//单价	2	单位0.01元/公里 BCD码(02H 20H==>2.20元/公里)
	public Segment<String> onMiles;//载客时行驶里程	2	单位0.1公里BCD码(01H 25H==>12.5公里)
	public Segment<String> waiteTime;//等候时间	3	包含时、分、秒 BCD码(00H 10H 30H表示等候时间为10分钟30秒)
	public Segment<String> money;//本次营运金额	3	单位0.01元BCD码(05H 60H 50H==>560.5元)
	public Segment<Integer> type;//交易方式	1	00代表现金交易 01代表刷卡消费

	
	private int s=0;
	
	public LZTAXI0x7e(ByteBuf bb){
		bb.skipBytes(s);
		bb.skipBytes(2);//跳过预留
		fid  =new Segment<Integer>(bb,1,new IntegerParse());
		driverNO   =new Segment<String>(bb,4,new StringParse());
		vehicleNO  =new Segment<String>(bb,6,new StringParse());
		offMiles   =new Segment<String>(bb,2,new StringParse());
		onDateTime =new Segment<String>(bb,6,new StringParse());
		offDateTime=new Segment<String>(bb,2,new StringParse());
		unitPrice  =new Segment<String>(bb,2,new StringParse());
		onMiles    =new Segment<String>(bb,2,new StringParse());
		waiteTime  =new Segment<String>(bb,3,new StringParse());
		money      =new Segment<String>(bb,3,new StringParse());
		type       =new Segment<Integer>(bb,1,new IntegerParse());
		//System.out.println(fid.value+"****"+Integer.parseInt(driverNO.value));
	}
	public static void main(String args[]){
		LinkedList<String> ls=FileUtil.readFile("d:\\gpslog\\lztaxi\\lztaxi0x7e.txt");
		for(Iterator<String> it=ls.iterator();it.hasNext();){
			String s=it.next();
			byte bs[]= new byte[s.length()/2];
			bs=Ecode.HexString2ByteArray(s);
			
			ByteBuf bb=Unpooled.buffer(bs.length);
			for(int i=0;i<bs.length;i++){
				bb.writeByte(bs[i]);
			}
			LZTAXI0x7e t=new LZTAXI0x7e(bb);
			//JT0x0200 jt=(JT0x0200)(t.convert());
			//log.info(jt);
		}
	}
}
