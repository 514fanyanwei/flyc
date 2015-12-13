package cc.gps.active.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.Recourse;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0201;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.lzbus.LZBus0x71;

public class JTProcess0x0201 extends JTProcess0x0200{
private static final Log log = LogFactory.getLog(JTProcess0x0201.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData data) {
		if(log.isDebugEnabled())
			log.info("处理来自"+data.head.phone+"的"+Recourse.getOrder("0x0201")+":-------------");
		
		
		//通知前端收到应答
		C2XAnswer td = new C2XAnswer();
		//对应平台消息的流水号
		int ptSerialID=data.bb.readUnsignedShort();
		//对应平台消息消息ID
		int ptMessageID=0x8201;  //是对0x8201的应答
		byte[] bs=super.processMessageBody(data);
		processAnswer(ptMessageID,ptSerialID,0,data.head.phone);
		
		
		td.head=(JTMessageHead)data.head; //必须有
		td.head.messageID=0x0001; //转为JT/T-808　的终端通用应答
		td.replySerialID=ptSerialID;
		//调用数据转发服务
		if(td.replySerialID<60000){  //小于6000转发
			processTrans(td);  //调用父类方法统一转发
			write2DB(td);//-------new
		}
		return bs;
	}
}