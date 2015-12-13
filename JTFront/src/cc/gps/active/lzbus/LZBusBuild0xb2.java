package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.util.Ecode;

public class LZBusBuild0xb2 extends LZBusBuildSendPacket {
	private static final Log log = LogFactory.getLog(LZBusBuild0xb2.class);
	
	public LZBusSendData sendData=new LZBusSendData();
	
	private String imgSerial; //照片序号
	private String num;//缺失切片序号
	
	public LZBusBuild0xb2(String imgSerial,String num){  //要求imgSerial传过来为4位字符串,num为2位字符串
		this.imgSerial=imgSerial;
		this.num=num;
	}
	@Override
	public  byte[] buildSendPacket(JTReceiveData data){
		
		sendData.body=buildMessageBody(data,65531);  //用65531作为序列号
		byte[] bs=sendData.toByte();
		//log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+JTBuild0x8001.getHexString(bs));
		return bs;
	}
	
	@Override
	protected String buildMessageBody(Object content, int serialID) {
		StringBuffer str = new StringBuffer();
		str.append(Ecode.DEC2HEX(serialID, 4));//下发指令的命令序列号
		str.append("b2");  //信息类别
		str.append(imgSerial);
		str.append(num);
		return str.toString();
	}

	@Override
	public boolean buildSendPacket(String clientID, Object content, int serialID) {
		// TODO Auto-generated method stub
		return false;
	}
}
