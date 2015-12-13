package cc.gps.data.lztaxi;

import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;

public class LZTAXIStation extends JTReceiveData{
	public JT0x0200 pos;
	public int stationID;    
	public String stationType;  //定点类型
	public int inout; //0　进站in   1 出站out
	/*定点类型：
	F	：停靠站
	f	：终点站
	I	：停车场
	i 	：加油站
	B	：起点站
    */
	
	public String toString(){
		return pos+"\n"+"sataionID:"+stationID+"\n"+
				"inout 0:  进入   1: 离开  :"+inout+"\n"+
				"stationType:"+stationType+"\n";
	}

}