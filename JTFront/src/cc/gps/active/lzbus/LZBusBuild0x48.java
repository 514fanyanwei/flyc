package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusLineVersion;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.util.Ecode;

public class LZBusBuild0x48 extends LZBusBuildSendPacket {
private static final Log log = LogFactory.getLog(LZBusBuild0x48.class);
	
	public LZBusSendData sendData=new LZBusSendData();
	//private static String order="";
	@Override
	public  byte[] buildSendPacket(JTReceiveData data){
		sendData.body=buildMessageBody(data,65532);  //用65532作为序列号
		byte[] bs=sendData.toByte();
		return bs;
	}
	
	//生成下发指令，该指令对所有终端相同
	@Override
	protected String buildMessageBody(Object content, int serialID) {
		StringBuffer str = new StringBuffer();
		str.append(Ecode.DEC2HEX(serialID, 4));//下发指令的命令序列号
		str.append("48");  //信息类别
		String ip=Ecode.S2A("218.70.24.166");
		while(ip.length()<40){
			ip+="00";
		}
		str.append(ip); //线路编号
		String port=Ecode.S2A("37778");
		while(port.length()<10){
			port+="00";
		}
		str.append(port);
		
		str.append("0000000000000000000000000000000000000000");
		str.append("0000000000");
		
		String name=Ecode.S2A("lzbus");
		while(name.length()<40){
			name+="00";
		}
		str.append(name);
		
		String psw=Ecode.S2A("lzbus");
		while(psw.length()<40){
			psw+="00";
		}
		str.append(psw);
		
		//log.info(str.toString());
		
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