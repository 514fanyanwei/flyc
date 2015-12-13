package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusDriverRequest;
import cc.gps.parse.lzbus.LZBus0x4a;
import cc.gps.parse.lzbus.LZBus0x72;
import cc.gps.util.Ecode;


public class LZBusProcess0x72 extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0x72.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		log.debug("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus0072")+":-------------");
		
		LZBusDriverRequest td=new LZBusDriverRequest();
		
		LZBus0x72 lz=new LZBus0x72(inData.bb,inData.head.len-3);
		td=lz.convert();
		td.head=inData.head; //必须有
		
		//调用数据转发服务
		processTrans(td);  //调用父类方法统一转发
		//写数据库
		write2DB(td);
		//向终端回送1个应答
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		return bs;
	}
}