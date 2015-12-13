package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;

public class LZBusProcess0x45  extends LZBusProcessReceive{
	private static final Log log = LogFactory.getLog(LZBusProcess0x45.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus0045")+":-------------");
		String body=inData.body;  //body为序列号后的实际内容
		log.debug("应答内容为"+body);
		log.info(body);
		
		return null;
	}
}