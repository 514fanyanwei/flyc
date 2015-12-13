package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusDriverRequest;
import cc.gps.parse.lzbus.LZBus0x4a;
import cc.gps.parse.lzbus.LZBus0x71;

//车载终端 对命令应答报文 
public class LZBusProcess0x71 extends LZBusProcessReceive{
	private static final Log log = LogFactory.getLog(LZBusProcess0x71.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData data) {
		if(log.isDebugEnabled())
			log.info("处理来自"+data.head.phone+"的"+Recourse.getOrder("lzbus0001")+":-------------");
		C2XAnswer td = new C2XAnswer();
		LZBus0x71 lz=new LZBus0x71(data.bb,data.head.messageID);
		td=(C2XAnswer)(lz.convert());
		/*
		if(data.head.messageID==0x71){   //报文本质是终端通用应答
			result=Integer.parseInt(body.substring(6,8),16);  
		}else{
			td.attachment=data;
		}*/
		
		//处理发向终端的指令的应答情况
		
		processAnswer(td.messageID,td.replySerialID,td.result,data.head.phone);
		
		
		//以下三项必须有
		td.head=(JTMessageHead)data.head.clone(); //必须有
		td.head.messageID=0x0001; //转为JT/T-808　的终端通用应答
		
		
		//调用数据转发服务
		if(td.replySerialID<60000){  //小于6000转发
			processTrans(td);  //调用父类方法统一转发
			write2DB(td);//-------new
		}
		return null;
	}
}
