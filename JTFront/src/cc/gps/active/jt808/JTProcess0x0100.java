package cc.gps.active.jt808;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0100;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.jt808.PJT0x0100;

public class JTProcess0x0100  extends JTProcessReceive{
	private static final Log log = LogFactory.getLog(JTProcess0x0100.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0100")+":-----------数据长:"+inData.head.len);
		
		
		PJT0x0100 pjt=new PJT0x0100(inData.bb,inData.head.len);
		JT0x0100 td=pjt.convert();
		td.head=inData.head;
		//log.info(td);
		//调用数据转发服务 
		processTrans(td);  //调用父类方法统一转发
		//2 回复终端1个平台通用应答
		//JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8100);
		byte[] bs=jtb.buildSendPacket(inData);
		return bs;
	}
}