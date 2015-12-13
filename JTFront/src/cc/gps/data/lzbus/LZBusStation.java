package cc.gps.data.lzbus;

import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;

public class LZBusStation extends JTReceiveData{
	//public  JT0x0200 pos=new JT0x0200();
	public JT0x0200 pos;
	public int stationID;    
	public double sInterval; //站间里程
	public String stationType;
	public int inout; //0　进站in   1 出站out
	public int upNumber;//上车人数
	public int downNumber;//下车人数
	public double je;//金额
	
	public int areaID=-1;//-1表示进出站报文  其他值为特殊站点报文
	
	public String toString(){
		return pos+"\n"+"sataionID:"+stationID+"\n"+
				"stationType:"+stationType+"\n"+
				"sInterval:"+sInterval+"\n"+
				"inout:"+inout+"\n"+
				"areaID:"+areaID;
	}

}
