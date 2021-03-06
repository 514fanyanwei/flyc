package cc.gps.active.lztaxi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.lzbus.LZBusBuildSendPacket;
import cc.gps.active.lzbus.LZBusBuildSendPacketFactory;
import cc.gps.active.lzbus.LZBusProcess0x71;
import cc.gps.active.lzbus.LZBusProcessReceive;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.lzbus.LZBus0x71;
import cc.gps.parse.lztaxi.LZTAXI0x4c;
import cc.gps.parse.lztaxi.LZTAXI0x53;

public class LZTAXIProcess0x53 extends LZTAXIProcessReceive{
	private static final Log log = LogFactory.getLog(LZTAXIProcess0x53.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData data) {
		if(log.isDebugEnabled())
			log.info("处理来自"+data.head.phone+"的"+Recourse.getOrder("lzbtaxi0053")+":-------------");
		C2XAnswer td = new C2XAnswer();
		LZTAXI0x53 lz=new LZTAXI0x53(data.bb);
		td=(C2XAnswer)(lz.convert());
		/*
		if(data.head.messageID==0x71){   //报文本质是终端通用应答
			result=Integer.parseInt(body.substring(6,8),16);  
		}else{
			td.attachment=data;
		}*/
		
		log.info(td.messageID+"  "+td.replySerialID+"  "+td.result+"  "+data.head.phone);
		//处理发向终端的指令的应答情况
		
		processAnswer(td.messageID,td.replySerialID,td.result,data.head.phone);
		
		
		//以下三项必须有
		td.head=(JTMessageHead)data.head.clone(); //必须有
		td.head.messageID=0x0001; //转为JT/T-808　的终端通用应答
		
		
		//调用数据转发服务
		if(td.replySerialID<60000){  //小于6000转发
			processTrans(td);  //调用父类方法统一转发
			//write2DB(td);//-------new
		}
		return null;
	}
}
