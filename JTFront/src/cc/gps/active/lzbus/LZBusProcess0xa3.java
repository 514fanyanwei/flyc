package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.util.Ecode;

public class LZBusProcess0xa3  extends LZBusProcessReceive{
	private static final Log log = LogFactory.getLog(LZBusProcess0x92.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus00a3")+":-------------");
		String body=inData.body;  //body为序列号后的实际内容
		return null;
	}
}