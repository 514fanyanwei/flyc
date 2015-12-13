package cc.gps.active.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0801;
import cc.gps.data.jt808.JTDataImage;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.jt808.PJT0x0801;
import cc.gps.parse.jt808.PJT0x0802;
import cc.gps.util.Ecode;

public class JTProcess0x0802 extends JTProcessReceive{
private static final Log log = LogFactory.getLog(JTProcess0x0802.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0802")+":-------------");
		
		ByteBuf bff=inData.bb.duplicate();
		
		while(bff.isReadable()){
			System.out.print(Ecode.DEC2HEX(bff.readUnsignedByte()));
		}System.out.println();
		
		//JTDataImage jt=
		PJT0x0802 pjt=new PJT0x0802(inData.bb,inData.head.len);
		pjt.convert();
		
		//处理发向终端的指令的应答情况
		//通知前端收到应答
		C2XAnswer ca = new C2XAnswer();
		//对应平台消息的流水号
		int ptSerialID=pjt.serialID.value;
		//对应平台消息消息ID
		int ptMessageID=0x8802;  //是对0x8104的应答
		processAnswer(ptMessageID,ptSerialID,0,inData.head.phone);
		
		
		//向终端回送1个平台通用应答
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		return bs;
		
		/*
		if(jt.unfinished.isEmpty()){
			//已采集完整
			JT0x0801 td = jt.jt0801;
			td.head=inData.head; //必须有
			td.gps.head.phone=inData.head.phone;
			log.info("终端上传的0X0801报文内容:"+td.mid+" * "+td.mtype+" * "+td.mecode+" * "+td.eventID+" * "+td.channelID+"\n");
			log.info("image:"+td.data);
			//调用数据转发服务
			processTrans(td);  //调用父类方法统一转发
			Global.JTCID2BodyArray.remove(inData.head.phone);
		}

		
		
		
		//向终端回送0x8800应答
		//JTBuild0x8800  jtb= new JTBuild0x8800();
		//byte[] bs=jtb.buildSendPacket(inData,jt.jt0801.mid,jt.unfinished);
		
		//向终端回送1个平台通用应答
		
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		return bs;*/
	}
}