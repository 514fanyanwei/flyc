package cc.gps.data.lzbus;

import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;

public class LZBusDriverRequest extends JTReceiveData{
	public JT0x0200 pos=new JT0x0200();
	public String requestType;
	public int stationID;
	public double sInterval; //站间里程
	public long id;//中心下发的非正常营运编号
	public double fmile=-1; //非正常运营里程   
	
	public String toString(){
		return pos+"\n"+
					"requestType:"+requestType+"\n"+
				"stationID:"+stationID+"\n"+
				"sInterval:"+sInterval+"\n"+
				"fmile:"+fmile+"\n"+
				"id:"+id;
	}

}
