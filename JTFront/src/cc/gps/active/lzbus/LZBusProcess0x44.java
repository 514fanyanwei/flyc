package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.JTReceiveData;

public class LZBusProcess0x44  extends LZBusProcessReceive{
	private static final Log log = LogFactory.getLog(LZBusProcess0x44.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus0044")+":-------------");
		String body=inData.body;  //body为序列号后的实际内容
		log.debug("应答内容为"+body);
		log.info(body);
		/*
		//1  
		//inData.body 由16进制字符串组成
		JT0x0200 td = new JT0x0200();
		//以下五项必须(前四项通用)
		td.head=inData.head; //必须有
		td.start=0x7e;//必须有
		td.clientID=inData.clientID;  //必须有
		//td.clientID="015213605049";  //必须有，测试用
		td.head.phone=td.clientID;
		//td.head.messageID=0x0200;
		
		
		log.info(td);
		*/
		//log.info(td);
		//0 验证鉴权码 ------------待做
		//1 组装向平台转发数据
		//2 向平台发送
		//3 回复终端通用应答
		
		//调用数据转发服务 
		//processTrans(td);  //调用父类方法统一转发
		
		//向终端回送1个平台通用应答
		//LSBuildSendPacket jtb=LSBuildSendPacketFactory.createBuild(0x80);??
		//byte[] bs=jtb.buildSendPacket(inData);??
		//向终端回送1个登录应答
		 //LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //终端登录应答				
		//写数据库
		//write2DB(td);
		
		//return bs;
		return null;
	}
}
