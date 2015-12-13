package cc.gps.data.lztaxi;

import cc.gps.data.jt808.JTReceiveData;

public class LZTAXIFee extends JTReceiveData{
	public int fid;//因为0x0006,0x00E8,0x0x00F2相同格式,故设记录messageID
	public String vno;//车牌号
	public String dwdm;//单位代码
	public String licence;
	public String onTime;//上车时间
	public String offTime; //下车时间
	public double miles;//计程公里数
	public double emiles;//空驶公里数
	public double eFee; //附加费
	public long waitTime;//等待计时时间  单位:分钟
	public double fee;
	public long cNO;//车次
	

	
	public String toString(){
		return "vno:" +vno+" dwdm:"+dwdm+" licence:"+licence+
				"\n onTime:"+onTime+" offTime:"+offTime+
				"\n miles:"+miles+" emptyKM:"+emiles+
				"\n eFee:"+eFee+" waitime:"+waitTime+
				"\n fee:"+fee+" cno车次(对应数据表中tnumber):"+cNO;
				
	}
	

}
