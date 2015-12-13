package cc.gps.data.lzbus;

import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;

public class LZBusLineVersion extends JTReceiveData{
	public String lineNO;
	public String lineVersion;
	
	public LZBusLineVersion(){
	}
	
	public String toString(){
		return 	"终端线路号:"+lineNO+"\n"+
				"终端线路版本号:"+lineVersion;
	}
}
