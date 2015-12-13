package cc.gps.active.lztaxi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.lzbus.LZBusBuild0x01;
import cc.gps.active.lzbus.LZBusBuildSendPacket;
import cc.gps.config.Global;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.data.lztaxi.LZTAXISendData;
import cc.gps.util.Ecode;

public class LZTAXIBuild0x80 extends LZTAXIBuildSendPacket {
	private static final Log log = LogFactory.getLog(LZTAXIBuild0x80.class);
	
	public LZTAXISendData sendData=new LZTAXISendData();
	@Override
	public  byte[] buildSendPacket(JTReceiveData data){
		//log.info(data.head.phone+"---*************----");
		sendData.body=buildMessageBody(data,65535);  //此处65535没用，主要是为匹配重载方法参数而设
		byte[] bs=sendData.toByte();
		//log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+JTBuild0x8001.getHexString(bs));
		return bs;
	}
	
	@Override
	protected String buildMessageBody(Object content, int serialID) {
		JTReceiveData data= (JTReceiveData)content;
		StringBuffer str = new StringBuffer();
		// 组装应答流水号  对应终端消息流水号
		int serial=data.head.serialID; 
		
		String temp=Ecode.DEC2HEX(serial);
		while(temp.length()<4) temp="0"+temp;
		
		str.append("80");  //信息类别
		
		str.append("1111"); //用户ID
		str.append("0001");  //指令序号
		str.append("01");  //登录成功标志
		//log.info(data.head.phone);
		//log.info(Global.VBHBySIM.get(data.head.phone));
		/*
		if(Global.VBHBySIM.get(data.head.phone)!=null){
			str.append(createUserID(Global.VBHBySIM.get(data.head.phone))); //用户ID
			str.append("0001");  //指令序号
			str.append("01");  //登录成功标志
		}else{
			str.append("0000"); //用户ID
			str.append("0001");  //指令序号
			str.append("00");  //登录失败标志
		}*/
			
		
		return str.toString();
	}

	@Override
	public boolean buildSendPacket(String clientID, Object content, int serialID) {
		// TODO Auto-generated method stub
		return false;
	}
	public static void main(String args[]){
		log.info(Ecode.DEC2HEX(1, 4));
	}
}

/*
extends LZTAXIBuildSendPacket {
	private static final Log log = LogFactory.getLog(LZTAXIBuild0x80.class);
	
	public LZTAXISendData sendData=new LZTAXISendData();
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
}*/