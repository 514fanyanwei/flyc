package cc.gps.active.lztaxi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;


public class LZTAXIProcess0x72  extends LZTAXIProcessReceive{
	private static final Log log = LogFactory.getLog(LZTAXIProcess0x72.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		//inData.bb内容从messageid的下一个字节开始
		log.debug("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lztaxi0072")+":-------------");
		
		//向终端回送1个在线保持包
		LZTAXIBuildSendPacket jtb=LZTAXIBuildSendPacketFactory.createBuild(0x78); //终端登录应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		return bs;
	}
}