package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusLineVersion;
import cc.gps.parse.lzbus.LZBus0x8c;
import cc.gps.util.Ecode;

public class LZBusProcess0x8c  extends LZBusProcessReceive{
	private static final Log log = LogFactory.getLog(LZBusProcess0x8c.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus008c")+":-------------");
		LZBus0x8c lz=new LZBus0x8c(inData.bb,inData.head.phone);
		LZBusLineVersion jt=lz.convert();
		//向终端回送1个应答
		inData.head.messageID=0x8c;
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x9949); //应答
		byte[] bs=jtb.buildSendPacket(jt); //bs为生成的可直接下发的指令，16进制byte[]
		/*
		for(int i=0;i<bs.length;i++){
			System.out.print(Ecode.DEC2HEX(bs[i],2)+"|");
		}
		System.out.println();*/
		return bs;
	}
}
