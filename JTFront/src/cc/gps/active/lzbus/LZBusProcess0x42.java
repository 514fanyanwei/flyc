package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;

public class LZBusProcess0x42  extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0x42.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus0042")+":-------------");
		super.processMessageBody(inData);
		
		//向终端回送1个应答
		inData.head.messageID=0x42;
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //终端登录应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		return bs;
	}
}
