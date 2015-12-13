package cc.gps.active.lztaxi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;

public class LZTAXIProcess0x41  extends LZTAXIProcess0x43{
	private static final Log log = LogFactory.getLog(LZTAXIProcess0x41.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lztaxi0041")+":-------------");
		return super.processMessageBody(inData);
		
	}
}
