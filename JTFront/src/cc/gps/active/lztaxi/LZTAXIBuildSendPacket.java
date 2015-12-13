package cc.gps.active.lztaxi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.IBuildSendPacket;
import cc.gps.active.lzbus.LZBusBuildSendPacket;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.data.lztaxi.LZTAXISendData;
import cc.gps.util.Ecode;

public class LZTAXIBuildSendPacket extends IBuildSendPacket{
	private static final Log log = LogFactory.getLog(LZTAXIBuildSendPacket.class);
	private int messageID;
	private LZTAXISendData sendData=new LZTAXISendData();

	public LZTAXIBuildSendPacket(){}
	
	public LZTAXIBuildSendPacket(int messageID){
		this.messageID=messageID;
	}
	//蓝斯设备产生发向终端的用户ID
	protected String createUserID(String u){
		String uid="";
		int x = Integer.parseInt(u);
		uid=Ecode.DEC2HEX(x, 4);
		return uid;
	}
	@Override
	public  boolean buildSendPacket(String clientID,Object content,int serialID){
		log.info("蓝斯出租报文下发0X"+Ecode.DEC2HEX(messageID));
		
		//1 生成消息头 ,供缓存下发指令检查用
		JTMessageHead head = new JTMessageHead();
		head.messageID=messageID;
		head.serialID=serialID;
		head.phone = clientID;
		sendData.head=head;
		//2  组装消息头 和 组装消息体
		sendData.body=(String)content;
		
		//return send(clientID,sendData,false);  //交给父类方法统一发送
		return send(clientID,sendData);  //交给父类方法统一发送
		
	}
	@Override
	protected String buildMessageBody(Object content, int serialID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}