package cc.gps.active.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JT0x0800;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.parse.jt808.PJT0x0200;
import cc.gps.parse.jt808.PJT0x0800;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;

public class JTProcess0x0800 extends JTProcessReceive{
private static final Log log = LogFactory.getLog(JTProcess0x0200.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0800")+":-------------");
		
		ByteBuf bff=inData.bb.duplicate();
		while(bff.isReadable()){
			System.out.print(Ecode.DEC2HEX(bff.readUnsignedByte()));
		}System.out.println();
		
		JT0x0800 td = (new PJT0x0800(inData.bb)).convert();
		td.head=inData.head; //必须有
		
		log.info("终端上传的0X0800报文内容:"+td.mid+" * "+td.mtype+" * "+td.mecode+" * "+td.eventID+" * "+td.channelID+"\n");
		//调用数据转发服务
		processTrans(td);  //调用父类方法统一转发
		

		/*
		//通知前端收到应答
		C2XAnswer ca = new C2XAnswer();
		//对应平台消息的流水号
		int ptSerialID=inData.bb.readUnsignedShort();
		//对应平台消息消息ID
		int ptMessageID=0x8201;  //是对0x8201的应答
		byte[] bs=super.processMessageBody(data);
		processAnswer(ptMessageID,ptSerialID,0,data.head.phone);*/
		
		
		//向终端回送1个平台通用应答
		
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		
		return bs;
	}
}