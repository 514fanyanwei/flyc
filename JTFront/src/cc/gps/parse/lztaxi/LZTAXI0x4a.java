package cc.gps.parse.lztaxi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.parse.lzbus.LZBus0xa1;
import cc.gps.util.Ecode;
import cc.gps.util.FileUtil;

public class LZTAXI0x4a  {

    public Segment<Integer> uid;//用户ID
    public Segment<Integer> oid;//指令序号
	
    public Segment<Integer> sid; //切片序号
	//public Segment<Integer> slen; //切片长度
	public Segment<String> scontent; //切片片内容 
	private static final Log log = LogFactory.getLog(LZBus0xa1.class);
	
	private int s=0;
	private String sim;
	public LZTAXI0x4a(ByteBuf bb,String sim,int len){
		
		bb.skipBytes(s);
		this.sim=sim;
		uid     =new Segment<Integer> (bb,2,new IntegerParse());
		oid     =new Segment<Integer> (bb,2,new IntegerParse());
		bb.skipBytes(3);
		sid     =new Segment<Integer>(bb,1,new IntegerParse());
		
		scontent=new Segment<String> (bb,len-4-1-4-2-1,new StringParse());
	}
	
	public LZBusImage convert(){
		LZBusImage lzImage=Global.CID2BodyArray.get(sim);
		log.debug(sid.value+"          *******          "+scontent.value);
		lzImage.image[sid.value]=filtXOR(scontent.value);
		boolean end=false;
		//log.info(lzImage.image[sid.value]);
		log.debug("组装第"+sid.value+"个切片完成");
		if(scontent.value.toLowerCase().contains("ffd90000")){  //可能结束
			
			/*
			buffer.mark();//记录当前位置
			if(buffer.getUnsignedShort()==0){ //后续为0认为结束
				end=true;
				break;
			}else
			buffer.reset();	//回溯*/
			end=true;
		}
		if(end) return lzImage;
		else return null;
	}
	
	private static String filtXOR(String str){
		byte [] bs=new byte[str.length()/2];
		byte[] temp=Ecode.HexString2ByteArray(str);
		int len=temp.length;
		for(int i=0;i<len;i++){
			//log.info(temp[i]+" ** ");
			bs[i]=temp[i];
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for(int j=0;j<bs.length;j++){
			if(bs[j]==0x1b){
				try {
					out.write(0x1b^bs[++j]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else
				out.write(bs[j]);
		}
		return Ecode.ByteArray2HexString(out.toByteArray(), -1, -1);
		
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

