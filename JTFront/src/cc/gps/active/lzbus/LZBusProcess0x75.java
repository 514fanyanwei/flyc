package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.lzbus.LZBus0x75;
import cc.gps.util.Ecode;

public class LZBusProcess0x75 extends LZBusProcessReceive{
	private static final Log log = LogFactory.getLog(LZBusProcess0x75.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		//inData.bb内容从messageid的下一个字节开始
		log.debug("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus0075")+":-------------");
		LZBus0x75 lz=new LZBus0x75(inData.bb);
		JT0x0102 td =lz.convert();
		td.head=inData.head;
		td.body=inData.body;
		
		Global.CID2Line.put(inData.head.phone,lz.lineNum.value);
		String driverNo=Global.getLastDriverNo("86"+inData.head.phone.substring(1));
		if(driverNo!=null){
			Global.CID2Driver.put(inData.head.phone, driverNo);
		}
		
		//log.debug(td);
		//log.debug(lz);
		//调用数据转发服务 
		processTrans(td);  //调用父类方法统一转发
		write2DB(td);
		//向终端回送1个登录应答
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //终端登录应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		return bs;
	}
}