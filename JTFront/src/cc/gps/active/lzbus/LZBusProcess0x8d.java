package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;

public class LZBusProcess0x8d  extends LZBusProcessReceive{
	private static final Log log = LogFactory.getLog(LZBusProcess0x8d.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus008d")+":-------------");
		

		//暂时向终端回送1个应答****************  待改
		inData.head.messageID=0x8d;
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x9948); //下发升级服务器信息
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		
		return bs;
	}
}
