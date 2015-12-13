package cc.gps.parse.jt808;

import java.util.Iterator;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.jt808.JT0x0800;
import cc.gps.data.jt808.JT0x0801;
import cc.gps.data.jt808.JTDataImage;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class PJT0x0801 {
	private static final Log log = LogFactory.getLog(PJT0x0801.class);
	
	public Segment<Long> mid; //多媒体数据ID
	public Segment<Integer> mtype; //多媒体类型
	public Segment<Integer> mecode; //多媒体格式编码
	public Segment<Integer> eventID; //事件项编码
	public Segment<Integer> channelID; //通道ID
	
	public PJT0x0200 pjt0x0200;
	
	public Segment<Long> pall; //数据总包数
	public Segment<Long> pid; //包ID
	public Segment<String> content; //多媒体数据包
	private int s=0; //开始位置  //测试时改为13
	
	private JTDataImage jtimage;
	public PJT0x0801(JTReceiveData inData){
		inData.bb.skipBytes(s);
		jtimage=(JTDataImage)Global.JTCID2BodyArray.get(inData.head.phone);
		if(jtimage==null){ //第1个包
			jtimage=new JTDataImage(inData.head.total);
			jtimage.total=inData.head.total;//总包数
			jtimage.image=new String[inData.head.total];
			mid     =new Segment<Long>(inData.bb,4,new LongParse());
			mtype   =new Segment<Integer>(inData.bb,1,new IntegerParse());
			mecode   =new Segment<Integer>(inData.bb,1,new IntegerParse());
			eventID=new Segment<Integer>(inData.bb,1,new IntegerParse());
			channelID    =new Segment<Integer>(inData.bb,1,new IntegerParse());
			pjt0x0200 = new PJT0x0200(inData.bb,true);
			content =new Segment<String>(inData.bb,inData.head.len-8-28,new StringParse());  //28 是pjt0x0200的长度
			
			jtimage.jt0801.mid=mid.value;
			jtimage.jt0801.mtype=mtype.value;
			jtimage.jt0801.mecode=mecode.value;
			jtimage.jt0801.eventID=eventID.value;
			jtimage.jt0801.channelID=channelID.value;
			jtimage.jt0801.gps=pjt0x0200.convert();
			//log.info(jtimage.jt0801.gps);
			Global.JTCID2BodyArray.put(inData.head.phone,jtimage);
		}else
		{
			content =new Segment<String>(inData.bb,inData.head.len,new StringParse()); 
		}
		jtimage.unfinished.remove(inData.head.packetID); //已收到的分组
		
		jtimage.image[inData.head.packetID-1]=content.value;  //分组从1开始计数 ,而数组下标从0开始
		//log.info(lzImage.image[sid.value]);
		log.info(inData.head.phone+"组装第"+inData.head.packetID+"个切片完成,共"+inData.head.total+"个");//58289  35055
		
		
		//pall     =new Segment<Long>(bb,4,new LongParse());
		//pid     =new Segment<Long>(bb,4,new LongParse());
		
	}
	
	public JTDataImage convert(){
		for(Iterator it=jtimage.unfinished.iterator();it.hasNext();)
		{
		  System.out.print(it.next()+"*");
		}System.out.println();
		if(!jtimage.unfinished.isEmpty()) return jtimage;
		
		for(int i=0;i<jtimage.total;i++){
			jtimage.jt0801.data+=jtimage.image[i];
		}
		return jtimage;
	}
		
}