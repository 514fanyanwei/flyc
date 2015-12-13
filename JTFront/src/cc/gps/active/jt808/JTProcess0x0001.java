package cc.gps.active.jt808;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.jt808.PJT0x0001;
import cc.gps.parse.lzbus.LZBus0x71;

//终端通用应答
public class JTProcess0x0001 extends JTProcessReceive{
	private static final Log log = LogFactory.getLog(JTProcess0x0001.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData data) {
		//if(log.isDebugEnabled())
		if(log.isDebugEnabled())
			log.info("处理来自"+data.head.phone+"的"+Recourse.getOrder("0x0001")+":-------------");
		
		C2XAnswer td = new PJT0x0001(data.bb).convert();
		
		td.head=(JTMessageHead)data.head; //必须有
		//td.head.phone=data.head.phone;
		td.head.messageID=0x0001; //转为JT/T-808　的终端通用应答
		//处理发向终端的指令的应答情况
		processAnswer(td.messageID,td.replySerialID,td.result,data.head.phone);
		
		//调用数据转发服务
		if(td.replySerialID<60000){  //小于6000转发
			processTrans(td);  //调用父类方法统一转发
			write2DB(td);//-------new
		}
		return null;
	}
}
