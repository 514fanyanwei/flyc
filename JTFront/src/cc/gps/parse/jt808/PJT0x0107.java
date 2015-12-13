package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0107 {
	public Segment<Integer> ctype; //终端类型
	public Segment<String> mid; //制造商 ID 
	public Segment<String> cmodel; //终端型号 
	public Segment<String> cid; //终端 ID
	public Segment<String> iccid; //终端 SIM 卡 ICCID  
	public Segment<Integer> hvlen; //终端硬件版本号长度
	public Segment<String> hversion; //终端硬件版本号 
	public Segment<Integer> fvlen; //终端固件版本号长度 //firmware 
	public Segment<String> fversion; //终端固件版本号 
	public Segment<Integer> gnss;//GNSS 模块属性
	public Segment<Integer> communication;//通信模块属性
	
	private int s=0; //开始位置  /测试时改为13
	
	
	public PJT0x0107(ByteBuf bb){
		bb.skipBytes(s);
		ctype   =new Segment<Integer>(bb,2,new IntegerParse());
		mid	    =new Segment<String>(bb,5,new StringParse());
		cmodel  =new Segment<String>(bb,20,new StringParse());
		cid     =new Segment<String>(bb,7,new StringParse());
		iccid   =new Segment<String>(bb,10,new StringParse());
		hvlen   =new Segment<Integer>(bb,1,new IntegerParse());
		hversion=new Segment<String>(bb,hvlen.value,new StringParse());
		fvlen	=new Segment<Integer>(bb,1,new IntegerParse());
		fversion=new Segment<String>(bb,fvlen.value,new StringParse());
		gnss    =new Segment<Integer>(bb,1,new IntegerParse());
		communication=new Segment<Integer>(bb,1,new IntegerParse());
		
	}
	
	public void convert(){
		System.out.println(
				"终端类型           "+ctype.value+   
				"\n制造商 ID          "+mid.value+                    
				"\n终端型号           "+cmodel.value+       
				"\n终端 ID            "+cid.value+                
				"\n终端 SIM 卡 ICCID  "+iccid.value+            
				"\n终端硬件版本号长度 "+hvlen.value+           
				"\n终端硬件版本号     "+hversion.value+         
				"\n终端固件版本号长度 "+fvlen.value+
				"\n终端固件版本号     "+fversion.value+
				"\nGNSS 模块属性"+gnss.value+
				"\n通信模块属性"+communication.value
				);
	}
}