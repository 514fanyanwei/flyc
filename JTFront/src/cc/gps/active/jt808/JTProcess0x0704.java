package cc.gps.active.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.jt808.PJT0x0704;
import cc.gps.parse.jt808.PJT0x0705;
import cc.gps.util.Ecode;

public class JTProcess0x0704  extends JTProcessReceive{
private static final Log log = LogFactory.getLog(JTProcess0x0704.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0704")+":-------------");
		
		ByteBuf bff=inData.bb.duplicate();
		
		while(bff.isReadable()){
			System.out.print(Ecode.DEC2HEX(bff.readUnsignedByte()));
		}System.out.println();
		
		//JTDataImage jt=
		PJT0x0704 pjt=new PJT0x0704(inData.bb);
		pjt.convert();
		
		//处理发向终端的指令的应答情况
		//通知前端收到应答
		/*
		C2XAnswer ca = new C2XAnswer();
		//对应平台消息的流水号
		int ptSerialID=pjt.serialID.value;
		//对应平台消息消息ID
		int ptMessageID=0x8900;  //是对0x8104的应答
		processAnswer(ptMessageID,ptSerialID,0,inData.head.phone);
		*/
		
		//向终端回送1个平台通用应答
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		return bs;
	}
}