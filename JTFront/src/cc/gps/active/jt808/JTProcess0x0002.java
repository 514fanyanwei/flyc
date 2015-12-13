package cc.gps.active.jt808;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Config;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0002;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;

//终端心跳
public class JTProcess0x0002 extends JTProcessReceive{
	private static final Log log = LogFactory.getLog(JTProcess0x0002.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {

		
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0002")+":-------------");
		
		//向终端回复1个平台通用应答,(必须回复)
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		
		return bs;
	}
	@Override
	public byte[] processMessageExtral(JTReceiveData data) {
		// TODO Auto-generated method stub
		return null;
	}

}
