package cc.gps.parse.lzbus;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cc.gps.active.lzbus.LZBusProcess0x72;
import cc.gps.config.Global;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;
import cc.gps.util.FileUtil;

public class LZBus0xa1 {
	public Segment<String> pid; //照片序号
	public Segment<Integer> sid; //切片序号
	public Segment<Integer> slen; //切片长度
	public Segment<String> scontent; //切片片内容 
	private static final Log log = LogFactory.getLog(LZBus0xa1.class);
	
	private int s=0;
	private String sim;
	public LZBus0xa1(ByteBuf bb,String sim){
		bb.skipBytes(s);
		this.sim=sim;
		pid     =new Segment<String> (bb,4,new StringParse());
		sid     =new Segment<Integer>(bb,2,new IntegerParse());
		slen    =new Segment<Integer>(bb,2,new IntegerParse());
		scontent=new Segment<String> (bb,slen.value,new StringParse());
	}
	
	public LZBusImage convert(){
		LZBusImage lzImage=Global.CID2BodyArray.get(sim);
		lzImage.image[sid.value]=scontent.value;
		//log.info(lzImage.image[sid.value]);
		log.debug("组装第"+sid.value+"个切片完成");
		return lzImage;
	}
	public static void main(String args[]){

		LinkedList<String> ls=FileUtil.readFile("d:\\gpslog\\lzbus0xa1.txt");
		
		for(Iterator<String> it=ls.iterator();it.hasNext();){
			String s=it.next();
			byte bs[]= new byte[s.length()/2];
			bs=Ecode.HexString2ByteArray(s);
			
			ByteBuf bb=Unpooled.buffer(bs.length);
			for(int i=0;i<bs.length;i++){
				bb.writeByte(bs[i]);
			}
			//LZBus0x41 t=new LZBus0x41(bb);
			//JT0x0200 jt=(JT0x0200)(t.convert());
			//log.info(jt);
			LZBus0xa1 t1=new LZBus0xa1(bb,"333");
			LZBusImage img=t1.convert();
			log.info(img.imgSerial+"  "+img.total+"  "+img.image.length);
		}
	}
}
