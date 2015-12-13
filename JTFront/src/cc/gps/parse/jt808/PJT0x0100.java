package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0100;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class PJT0x0100 {
	public Segment<Long>   provinceID; //省域 ID 
	public Segment<Long>   countyID; //市县域 ID 
	public Segment<String> manufacturerID; //制造商 ID 
	public Segment<String> deviceType; //终端型号
	public Segment<String> deviceID; //终端 ID 
	public Segment<Long>   plateColor; //车牌颜色 
	public Segment<String> vid; //车辆标识 
	
	private int s=0; //开始位置  //测试时改为13
	
	
	public PJT0x0100(ByteBuf bb,int len){
		bb.skipBytes(s);
		provinceID    =new Segment<Long>(bb,2,new LongParse()); //省域 ID 
		countyID      =new Segment<Long>(bb,2,new LongParse()); //市县域 ID 
		manufacturerID=new Segment<String>(bb,5,new StringParse()); //制造商 ID 
		deviceType    =new Segment<String>(bb,20,new StringParse()); //终端型号
		deviceID      =new Segment<String>(bb,7,new StringParse());//终端 ID 
		plateColor    =new Segment<Long>(bb,1,new LongParse());//车牌颜色 
		vid           =new Segment<String>(bb,len-(2+2+5+20+7+1),new StringParse());//车辆标识 
		
	}
	
	public JT0x0100 convert(){
		JT0x0100 jt=new JT0x0100();
		jt.provinceID=provinceID.value.intValue(); //省域 ID 
		jt.countyID=countyID.value.intValue(); //市县域 ID 
		jt.manufacturerID=Ecode.A2S(manufacturerID.value); //制造商 ID 
		jt.deviceType=Ecode.A2S(deviceType.value); //终端型号
		jt.deviceID=Ecode.A2S(deviceID.value); //终端 ID 
		jt.plateColor=plateColor.value.intValue(); //车牌颜色 
		jt.vid=vid.value; //车辆标识 
		
		return jt;
	}
}