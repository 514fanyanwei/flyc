package cc.gps.parse.lzbus;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cc.gps.config.Global;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;
import cc.gps.util.FileUtil;

public class LZBus0xa0 {//必须先解析LZBus0x41??
	private static final Log log = LogFactory.getLog(LZBus0xa0.class);
	public Segment<String> pid; //照片序号
	public Segment<Integer> ptype; //照片类型
	public Segment<Integer> psize; //照片尺寸
	public Segment<Integer> pno; //摄像头号
	public Segment<Integer> plen; //照片大小 
	public Segment<Integer> psnum; //总切片数
	public Segment<String> preason;//拍照原因
	
	private int s=26;
	private String sim;
	public LZBus0xa0(ByteBuf bb,String sim){
		bb.skipBytes(s);
		this.sim=sim;
		pid     =new Segment<String>(bb,4,new StringParse());
		ptype   =new Segment<Integer>(bb,1,new IntegerParse());
		psize   =new Segment<Integer>(bb,1,new IntegerParse());
		pno     =new Segment<Integer>(bb,1,new IntegerParse());
		plen    =new Segment<Integer>(bb,4,new IntegerParse());
		psnum   =new Segment<Integer>(bb,2,new IntegerParse());
		preason =new Segment<String> (bb,1,new StringParse());
	}
	
	public LZBusImage convert(){
		Global.CID2BodyArray.remove(sim);
		LZBusImage lzImage=new LZBusImage();
		lzImage.imgSerial=pid.value;
		lzImage.image=new String[psnum.value];
		lzImage.total=psnum.value;
		Global.CID2BodyArray.put(sim, lzImage);
		Global.CID2NUM.put(sim, plen.value);
		return lzImage;
	}
	public static void main(String args[]){

		LinkedList<String> ls=FileUtil.readFile("d:\\gpslog\\lzbus0xa0.txt");
		
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
			LZBus0xa0 t1=new LZBus0xa0(bb,"333");
			LZBusImage img=t1.convert();
			log.info(img.imgSerial+"  "+img.total+"  "+img.image.length);
		}
	}

}
