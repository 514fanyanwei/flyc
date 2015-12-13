package cc.gps.parse.lzbus;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.data.lzbus.LZBusStation;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;
import cc.gps.util.FileUtil;

public class LZBus0x79 {
	private static final Log log = LogFactory.getLog(LZBus0x79.class);
	public Segment<Integer> stationID; //站点编号  
	public Segment<String> stationType; //定点类型
	public Segment<Integer> inout; //进站或出站
	
	private int s=30; //开始位置 //测试36 
	public LZBus0x79(ByteBuf bb){
		bb.skipBytes(s);
		stationID    =new Segment<Integer>(bb,2,new IntegerParse());
		bb.skipBytes(3);
		stationType  =new Segment<String>(bb,1,new StringParse());
		inout        =new Segment<Integer>(bb,1,new IntegerParse());
	}
	
	public LZBusStation convert(){
		LZBusStation st=new LZBusStation();
		st.stationType=Ecode.A2S(stationType.value);
		st.inout=inout.value;
		st.stationID=stationID.value;
		return st;
	}
	
	public static void main(String args[]){

		LinkedList<String> ls=FileUtil.readFile("d:\\gpslog\\lzbus0x79.txt");
		
		for(Iterator<String> it=ls.iterator();it.hasNext();){
			String s=it.next();
			byte bs[]= new byte[s.length()/2];
			bs=Ecode.HexString2ByteArray(s);
			
			ByteBuf bb=Unpooled.buffer(bs.length);
			for(int i=0;i<bs.length;i++){
				bb.writeByte(bs[i]);
			}
			
			LZBus0x79 t1=new LZBus0x79(bb);
			LZBusStation lz=t1.convert();
			log.info(lz);
		}
	}
}
