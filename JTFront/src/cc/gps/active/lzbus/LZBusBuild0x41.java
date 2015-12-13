package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.util.Ecode;
//平台下发心跳包
public class LZBusBuild0x41 extends LZBusBuildSendPacket {
	private static final Log log = LogFactory.getLog(LZBusBuild0x41.class);
	
	public LZBusSendData sendData=new LZBusSendData();
	@Override
	public  byte[] buildSendPacket(JTReceiveData data){
		
		sendData.body=buildMessageBody(data,65534);  //用65534作为序列号
		byte[] bs=sendData.toByte();
		//log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+JTBuild0x8001.getHexString(bs));
		return bs;
	}
	
	@Override
	protected String buildMessageBody(Object content, int serialID) {
		StringBuffer str = new StringBuffer();
		str.append(Ecode.DEC2HEX(serialID, 4));//下发指令的命令序列号
		str.append("41");  //信息类别
		
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
