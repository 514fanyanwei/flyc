package cc.gps.data.jt808;

import cc.gps.parse.Segment;

public class JT0x0100 extends JTReceiveData{
	public int   provinceID; //省域 ID 
	public int   countyID; //市县域 ID 
	public String manufacturerID; //制造商 ID 
	public String deviceType; //终端型号
	public String deviceID; //终端 ID 
	public int   plateColor; //车牌颜色 
	public String vid; //车辆标识
	
	public String toString(){
		return "省域 ID  "+provinceID+
				"\n市县域 ID"+countyID+   
				"\n制造商 ID"+manufacturerID+
				"\n终端型号 "+deviceType+
				"\n终端 ID  "+deviceID+   
				"\n车牌颜色 "+plateColor+   
				"\n车辆标识 "+vid;    
	}
}
