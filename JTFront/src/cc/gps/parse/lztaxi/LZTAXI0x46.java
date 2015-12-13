package cc.gps.parse.lztaxi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.lzbus.LZBusStation;
import cc.gps.data.lztaxi.LZTAXIStation;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.parse.lzbus.LZBus0x79;
import cc.gps.parse.lzbus.LZBus0x7e;
import cc.gps.util.Ecode;
import cc.gps.util.FileUtil;

public class LZTAXI0x46 {
	private static final Log log = LogFactory.getLog(LZTAXI0x46.class);
	public Segment<Integer> stationID; //站点编号  
	public Segment<String> stationType; //定点类型
	
	//private int s=30; //开始位置 //测试36 
	public LZTAXI0x46(ByteBuf bb){
		//bb.skipBytes(s);
		stationID    =new Segment<Integer>(bb,2,new IntegerParse());
		stationType  =new Segment<String>(bb,1,new StringParse());
		
	}
	
	public LZTAXIStation convert(){
		LZTAXIStation st=new LZTAXIStation();
		st.stationType=Ecode.A2S(stationType.value);
		st.stationID=stationID.value;
		return st;
	}
	
	public static void main(String args[]){
		LinkedList<String> ls=FileUtil.readFile("d:\\gpslog\\lzbus0x7e.txt");
		for(Iterator<String> it=ls.iterator();it.hasNext();){
			String s=it.next();
			byte bs[]= new byte[s.length()/2];
			bs=Ecode.HexString2ByteArray(s);
			ByteBuf bb=Unpooled.buffer(bs.length);
			for(int i=0;i<bs.length;i++){
				bb.writeByte(bs[i]);
			}
			LZBus0x7e t1=new LZBus0x7e(bb);
			LZBusStation lz=t1.convert();
			log.info(lz);
		}
	}
}