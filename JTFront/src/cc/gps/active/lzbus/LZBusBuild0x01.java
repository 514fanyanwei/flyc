package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.util.Ecode;

public class LZBusBuild0x01 extends LZBusBuildSendPacket {
	private static final Log log = LogFactory.getLog(LZBusBuild0x01.class);
	
	public LZBusSendData sendData=new LZBusSendData();
	@Override
	public  byte[] buildSendPacket(JTReceiveData data){
		//log.info(data.head.phone+"---*************----");
		if(data.head.messageID==258) data.head.messageID=0x75;  //登陆从0x102改为0x75
		sendData.body=buildMessageBody(data);  //用65535作为对登陆的应答序列号
		byte[] bs=sendData.toByte();
		
		//log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+JTBuild0x8001.getHexString(bs));
		return bs;
	}
	
	//@Override
	protected String buildMessageBody(Object content) {
		JTReceiveData data= (JTReceiveData)content;
		StringBuffer str = new StringBuffer();
		// 组装应答流水号  对应终端消息流水号
		int serial=data.head.serialID; 
		
		
		str.append(Ecode.DEC2HEX(serial+1, 4));//下发指令的命令序列号
		str.append("01");  //信息类别
		str.append(Ecode.DEC2HEX(serial,4));//对应终端登陆请求报文的应答序列号
		//log.info(Ecode.DEC2HEX(serial,4)+"*****");
		str.append(Ecode.DEC2HEX(data.head.messageID,2));  //车载终端上发指令的 ID 
		str.append("00"); //处理结果  0 成功      1失败
		
		return str.toString();
	}

	@Override
	public boolean buildSendPacket(String clientID, Object content, int serialID) {
		// TODO Auto-generated method stub
		return false;
	}
	public static void main(String args[]){
		log.info(Ecode.DEC2HEX(65535, 4));
	}
}
