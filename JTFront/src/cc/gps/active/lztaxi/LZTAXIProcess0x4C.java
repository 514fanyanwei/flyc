package cc.gps.active.lztaxi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.lzbus.LZBusBuildSendPacket;
import cc.gps.active.lzbus.LZBusBuildSendPacketFactory;
import cc.gps.active.lzbus.LZBusProcess0x71;
import cc.gps.active.lzbus.LZBusProcess0x75;
import cc.gps.active.lzbus.LZBusProcessReceive;
import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.lzbus.LZBus0x71;
import cc.gps.parse.lzbus.LZBus0x75;
import cc.gps.parse.lztaxi.LZTAXI0x4c;

public class LZTAXIProcess0x4C extends LZTAXIProcessReceive{
	private static final Log log = LogFactory.getLog(LZTAXIProcess0x4C.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		//inData.bb内容从messageid的下一个字节开始
		log.debug("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lztaxi004c")+":-------------");
		LZTAXI0x4c lz=new LZTAXI0x4c(inData.bb);
		JT0x0102 td =lz.convert();
		td.head=inData.head;
		td.body=inData.body;
		
		//Global.CID2Line.put(inData.head.phone,lz.lineNum.value);
		
		log.debug(td);
		//log.debug(lz);
		//调用数据转发服务 
		processTrans(td);  //调用父类方法统一转发
		write2DB(td);
		//向终端回送1个登录应答
		LZTAXIBuildSendPacket jtb=LZTAXIBuildSendPacketFactory.createBuild(0x80); //终端登录应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		return bs;
	}
}