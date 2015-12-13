package cc.gps.active.jt808;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0104;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.parse.jt808.PJT0x0104;
import cc.gps.parse.jt808.PJT0x0200;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;

//查询终端参数应答
public class JTProcess0x0104 extends JTProcessReceive{
	private static final Log log = LogFactory.getLog(JTProcess0x0104.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		log.debug("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0104")+":-------------");
		
		//inData.body 由16进制字符串组成
		
		ByteBuf bff=inData.bb.duplicate();
		/*
		while(bff.isReadable()){
			System.out.print(Ecode.DEC2HEX(bff.readUnsignedByte())+"|");
		}System.out.println();*/
		PJT0x0104 pjt=new PJT0x0104(inData.bb);
		JT0x0104 td=pjt.convert();
		td.head=inData.head;

		//处理发向终端的指令的应答情况
		//通知前端收到应答
		C2XAnswer ca = new C2XAnswer();
		//对应平台消息的流水号
		int ptSerialID=bff.readUnsignedShort();
		//对应平台消息消息ID
		int ptMessageID=0x8104;  //是对0x8104的应答
		processAnswer(ptMessageID,ptSerialID,0,inData.head.phone);
		
		//调用数据转发服务                         
		processTrans(td);  //调用父类方法统一转发
		write2DB(td);//-------new
		
		
		//向终端回送1个平台通用应答
		
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		return bs;
		//return null;

	}
	@Override
	public byte[] processMessageExtral(JTReceiveData data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
