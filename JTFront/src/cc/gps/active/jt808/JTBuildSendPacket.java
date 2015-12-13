package cc.gps.active.jt808;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//组装发送数据的父类
import cc.gps.active.IBuildSendPacket;
import cc.gps.active.lzbus.LZBusBuildSendPacket;
import cc.gps.config.Global;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTSendData;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.util.Ecode;

/**
 * 该类负责向终端下发数据,包括两种下发：
 * 1.对终端发到服务器数据的回应，该类回应不需终端应答  byte[] buildSendPacket(JTReceiveData data)
 * 2.转发平台向终端下发的指令，需终端应答  boolean buildSendPacket(String clientID,Object content)
 */
public class JTBuildSendPacket  extends IBuildSendPacket{
	private static final Log log = LogFactory.getLog(LZBusBuildSendPacket.class);
	private int messageID;
	private JTSendData sendData=new JTSendData();

	public JTBuildSendPacket(){}
	
	public JTBuildSendPacket(int messageID){
		this.messageID=messageID;
	}
	
	@Override
	public  boolean buildSendPacket(String clientID,Object content,int serialID){ 
		//serialID不能在前端生成,因为检验应答时需要serialID,无法检验应答
		log.info("向交通部设备下发报文0X"+Ecode.DEC2HEX(messageID));
		
		
		//1 生成消息头 ,供缓存下发指令检查用
		JTMessageHead head = new JTMessageHead();
		head.messageID=messageID;
		head.serialID=serialID;
		//head.serialID=Global.creatSID(head.phone);
		head.phone = clientID;
		sendData.head=head;
		//2  组装消息头 和 组装消息体
		if(content!=null)
		sendData.body=(String)content;
		System.out.println(sendData.body+"*******");
		head.len=sendData.body.length()/2;
		//log.info("send head:"+sendData.head);
		//return send(clientID,sendData,false);  //交给父类方法统一发送
		return send(clientID,sendData);  //交给父类方法统一发送
		
	}
	@Override
	protected String buildMessageBody(Object content, int serialID) {
		// TODO Auto-generated method stub
		return null;
	}
}
