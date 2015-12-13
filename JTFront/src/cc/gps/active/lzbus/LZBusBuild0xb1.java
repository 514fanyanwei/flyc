package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.util.Ecode;

public class LZBusBuild0xb1 extends LZBusBuildSendPacket {
	private static final Log log = LogFactory.getLog(LZBusBuild0xb1.class);
	
	public LZBusSendData sendData=new LZBusSendData();
	
	private String imgSerial; //照片序号
	
	public LZBusBuild0xb1(String imgSerial){
		this.imgSerial=imgSerial;
	}
	@Override
	public  byte[] buildSendPacket(JTReceiveData data){
		
		sendData.body=buildMessageBody(data,65530);  //用65530作为序列号
		byte[] bs=sendData.toByte();
		return bs;
	}
	
	@Override
	protected String buildMessageBody(Object content, int serialID) {
		StringBuffer str = new StringBuffer();
		str.append(Ecode.DEC2HEX(serialID, 4));//下发指令的命令序列号
		str.append("b1");  //信息类别
		str.append(imgSerial);  //信息类别
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
