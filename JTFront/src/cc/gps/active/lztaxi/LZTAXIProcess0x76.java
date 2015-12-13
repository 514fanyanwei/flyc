package cc.gps.active.lztaxi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;

public class LZTAXIProcess0x76 extends LZTAXIProcessReceive{
	private static final Log log = LogFactory.getLog(LZTAXIProcess0x76.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lztaxi0076")+":-------------");
		
		return null;
	}
	
}