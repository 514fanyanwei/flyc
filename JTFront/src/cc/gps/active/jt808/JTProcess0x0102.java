package cc.gps.active.jt808;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.parse.lzbus.LZBus0x41;
import cc.gps.service.TransSessionManager;
import cc.gps.util.Ecode;
import cc.gps.parse.jt808.PJT0x0102;

//终端鉴权
public class JTProcess0x0102 extends JTProcessReceive{
	private static final Log log = LogFactory.getLog(JTProcess0x0102.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0102")+":-----------数据长:"+inData.head.len);
		
		//0 验证鉴权码 ------------待做
		//1 组装向平台转发数据
		//2 向平台发送
		//3 回复终端通用应答
		PJT0x0102 pjt=new PJT0x0102(inData.bb,inData.head.len);
		JT0x0102 td=pjt.convert();
		td.head=inData.head;
		//td.authorization_code=Ecode.A2S(inData.body);//不同业务，此处可能不同
		//log.info(td.authorization_code+"********");
		//调用数据转发服务 
		processTrans(td);  //调用父类方法统一转发
		
		//2 回复终端1个平台通用应答
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		
		return bs;
	}
	

}
