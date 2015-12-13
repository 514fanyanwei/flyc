package cc.gps.parse.jt808;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.parse.DateTimeParse;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.parse.lzbus.LZBus0xa0;
import cc.gps.util.Ecode;
import cc.gps.util.FileUtil;

public class PJT0x0200 {
	private static final Log log = LogFactory.getLog(PJT0x0200.class);
	public Segment<Long> iwarning; //报警标志
	public Segment<Long> istatus; //状态
	public Segment<Long> latitude; //纬度
	public Segment<Long> longitude; //经度
	public Segment<Long> altitude; //高程,单位 米
	public Segment<Long> velocity; //速度  单位:1/10km/h
	public Segment<Long> orientation; //方向
	public Segment<String> time; //时间
	public HashMap<Integer,String> more=new HashMap<Integer,String>();//附加信息 
		
	private int s=0; //开始位置  //测试时改为13
	
	
	public PJT0x0200(ByteBuf bb,boolean inOther){ //inOther=false 独立位置信息0x0200  true:其他报文附带的位置信息 比如:0x0801
		
		bb.skipBytes(s);
		istatus    =new Segment<Long>(bb,4,new LongParse());
		iwarning   =new Segment<Long>(bb,4,new LongParse());
		latitude   =new Segment<Long>(bb,4,new LongParse());
		longitude  =new Segment<Long>(bb,4,new LongParse());
		
		altitude   =new Segment<Long>(bb,2,new LongParse());
		velocity   =new Segment<Long>(bb,2,new LongParse());
		orientation=new Segment<Long>(bb,2,new LongParse());
		time	   =new Segment<String>(bb,6,new DateTimeParse());
		
		while(bb.readableBytes()>2&&inOther==false){
			int eid=bb.readByte();
			int elen=bb.readByte();
			//log.info(eid+"********"+elen);
			more.put(eid,(new Segment<String>(bb,elen,new StringParse())).value);
			//log.info(more.get(eid));
		}
	}
	
	public JT0x0200 convert(){
		JT0x0200 td=new JT0x0200();
		td.iwarning=iwarning.value;
		td.istatus=istatus.value;
		
		td.latitude=compute(latitude.value);
		td.longitude=compute(longitude.value);
		td.altitude=altitude.value;
		td.velocity=velocity.value/10;
		td.orientation=orientation.value;
		td.time="20"+time.value;
		td.status=Ecode.DEC2HEX(td.istatus,8);
		td.warning=Ecode.DEC2HEX(td.iwarning,8);
		
		Iterator<Integer> iterator = more.keySet().iterator();
		while(iterator.hasNext()) {
			int eid=iterator.next();
			switch(eid){
			case 0x01:td.mileage=Integer.parseInt(Ecode.HEX2DEC(more.get(eid)));break;
			case 0x02:td.oilmass=Integer.parseInt(Ecode.HEX2DEC(more.get(eid)));break;
			case 0x03:td.extSpeed=Integer.parseInt(Ecode.HEX2DEC(more.get(eid)));break;
			case 0x04:td.eventID=Integer.parseInt(Ecode.HEX2DEC(more.get(eid)));break;
			case 0x11:{   //超速报警附加信息
				String s=more.get(eid);
				td.osPosID=Integer.parseInt(s.substring(0,2));  //不会超过10 所以不需Ecode.HEX2DEC()
				if(td.osPosID!=0){
					td.osAreaID=Long.parseLong(Ecode.HEX2DEC(s.substring(2)));//区域或线路 ID 
				}
				break;
			}
			case 0x12:{//进出区域/路线报警附加信息
				String s=more.get(eid);
				td.oiPosID=Integer.parseInt(s.substring(0,2));//位置类型
				td.oiAreaID=Long.parseLong(Ecode.HEX2DEC(s.substring(2,10))); //区域或线路 ID 
				td.oOri=Integer.parseInt(s.substring(10)); //进/出
				break;
			}
			case 0x13:{//路线行驶时间不足/ 过长报警附加信息
				String s=more.get(eid);
				td.lineID=Long.parseLong(Ecode.HEX2DEC(s.substring(0,8)));//路段 ID
				td.driveTime=Integer.parseInt(Ecode.HEX2DEC(s.substring(8,12))); //路段行驶时间(s)
				td.driveResult=Integer.parseInt(s.substring(12)); //结果  0：不足；1：过长
				break;
			}
			case 0x25:td.signal=Long.parseLong(Ecode.HEX2DEC(more.get(eid)));break;
			case 0x2a:td.io=Integer.parseInt(Ecode.HEX2DEC(more.get(eid)));break;
			case 0x2b:td.analog=Long.parseLong(Ecode.HEX2DEC(more.get(eid)));break;
			case 0x30:td.signal_intensity=Integer.parseInt(Ecode.HEX2DEC(more.get(eid)));break;
			case 0x31:td.satellite_num=Integer.parseInt(Ecode.HEX2DEC(more.get(eid)));break;
			}
		}
		return td;
	}
	public double compute(long val){
		double value=0;
		value=val/Math.pow(10, 6);
		return value;
	}
	public static void main(String args[]){
		LinkedList<String> ls=FileUtil.readFile("d:\\gpslog\\jttest\\jt0x0200.txt");
		
		for(Iterator<String> it=ls.iterator();it.hasNext();){
			String s=it.next();
			byte bs[]= new byte[s.length()/2];
			bs=Ecode.HexString2ByteArray(s);
			
			ByteBuf bb=Unpooled.buffer(bs.length);
			for(int i=0;i<bs.length;i++){
				bb.writeByte(bs[i]);
			}
			PJT0x0200 t=new PJT0x0200(bb,true);
			JT0x0200 jt=(JT0x0200)(t.convert());
			log.info(jt);
		}
		
	}
}
