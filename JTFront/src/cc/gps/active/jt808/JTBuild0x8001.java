package cc.gps.active.jt808;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.JTSendData;
import cc.gps.util.Ecode;
import cc.gps.util.Utility;

//平台通用应答
public class JTBuild0x8001 extends JTBuildSendPacket {
	private static final Log log = LogFactory.getLog(JTBuild0x8001.class);
	
	public JTSendData sendData=new JTSendData();
	@Override
	public  byte[] buildSendPacket(JTReceiveData data){
		sendData.head=buildMessageHead(data.head);
		sendData.body=buildMessageBody(data,65535);  //此处65535没用，主要是为匹配重载方法参数而设
		//log.debug("JTBuild0x8001,send head:"+sendData.head.toStrings());
		byte[] bs=sendData.toByte();
		//log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+JTBuild0x8001.getHexString(bs));
		return bs;
	}
	@Override
	public JTMessageHead buildMessageHead(JTMessageHead rhead){
		JTMessageHead head = new JTMessageHead();
		head.messageID=0x8001;

		head.len=0x05;
		head.des=0;
		head.more=0;
		head.phone=rhead.phone;
		//head.serialID=Utility.getSendCount();
		//head.serialID=Global.creatSID(rhead.phone);
		head.serialID=65535;//所有平台应答都用65535作流水号，其他指令的流水号为0-65534
		//head.serialID=1;//所有平台应答都用1作流水号，其他指令的流水号为2-65534
		

		return head;
	}
	@Override
	public String buildMessageBody(Object rdata,int serialID){//此处serialID没用，主要是为匹配重载方法参数而设
		JTReceiveData data= (JTReceiveData)rdata;
		StringBuffer str = new StringBuffer();
		// 组装应答流水号  对应终端消息流水号
		int serial=data.head.serialID; 
		
		String temp=Ecode.DEC2HEX(serial,4);
		//while(temp.length()<4) temp="0"+temp;
		//log.info(serial+"  "+temp);
		str.append(temp);
		// 组装应答ID  对应终端的消息ID
		temp=Ecode.DEC2HEX(data.head.messageID);
		while(temp.length()<4) temp="0"+temp;
		str.append(temp);
		str.append("00");
		
		//log.info(str+"---------***----------");
		return str.toString();
	}
	
	public static String getHexString(byte[] b) {
		  String result = "";
		  for (int i=0; i < b.length; i++) {
		    result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		  }
		  return result;
		}
	@Override
	public boolean buildSendPacket(String clientID, Object content, int serialID) {
		// TODO Auto-generated method stub
		return false;
	}
}
