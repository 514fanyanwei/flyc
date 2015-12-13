package cc.gps.data.lzbus;

import cc.gps.data.jt808.JT0x0200;

public class LZBusOverSpeedTime {
	public JT0x0200 pos=new JT0x0200();
	public int stationID;
	public double sInterval; //站间里程
	public int time;
	
	public String toString(){
		return pos+"\n"+
					"time:"+time+"\n"+
					"stationID:"+stationID+"\n"+
					"sInterval:"+sInterval+"\n";
				
	}

}
