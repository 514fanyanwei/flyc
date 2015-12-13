package cc.gps.data.lzbus;

import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.*;
public class LZBusDriver extends JTReceiveData{
	
	public JT0x0200 pos;
	public int stationID;    
	public double sInterval; //站间里程
	public String driverID;
	public String psw;
	public int login;  //1  签到   0签退
 	
	public LZBusDriver(){
		pos=new JT0x0200();
	}
	
	public String toString(){
		return pos+"\n"+
					"stationID:"+stationID+"\n"+
				"sInterval:"+sInterval+"\n"+
				"driverID:"+driverID+"\n"+
				"psw:"+psw+"\n"+
				"login:"+login+"\n";
	}
	

}
