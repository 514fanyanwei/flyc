package cc.gps.active.jt808;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0700;
import cc.gps.data.jt808.JT0x0a00;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.jt808.PJT0x0A00;

public class JTProcess0x0a00 extends JTProcessReceive{
	private static final Log log = LogFactory.getLog(JTProcess0x0a00.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		log.debug("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0a00")+":-------------");
		JT0x0a00 td=(new PJT0x0A00(inData.bb,inData.head.len)).convert();
		td.head=inData.head; //必须有
		
		//调用数据转发服务 
		processTrans(td);  //调用父类方法统一转发
		//写数据库
		write2DB(td);
			
		//向终端回送1个平台通用应答
		
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		return bs;
		
		/*
		String body = data.body;
		log.debug("上传的行驶记录数据："+body);
		
		
		//对应平台消息的流水号
		int ptSerialID=Integer.parseInt(body.substring(0,4),16);
		//对应平台消息消息ID
		int ptMessageID=0x8700;  //是对0x8700的应答
		
		
		//处理对平台下发指令的应答  (待验证)
		String clientID=data.clientID;
		processAnswer(ptMessageID,ptSerialID,0,clientID);
		
		
		JT0x0700 td = new JT0x0700();
		//以下三项通用
		td.head=data.head; //必须有
		td.start=0x7e;//必须有
		td.clientID=data.clientID; //必须有
		
		td.body=body;
		td.replySerialID=ptSerialID;
		td.order=Integer.parseInt(body.substring(4,6),16);
		td.content=body.substring(6);
		
		log.info(td.replySerialID+"  "+td.order+"  "+td.content);
		//调用数据转发服务
		processTrans(td);  //调用父类方法统一转发
		
		//向终端回复1个平台通用应答
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(data);
		return bs;*/

	}
	@Override
	public byte[] processMessageExtral(JTReceiveData data) {
		// TODO Auto-generated method stub
		return null;
	}
}