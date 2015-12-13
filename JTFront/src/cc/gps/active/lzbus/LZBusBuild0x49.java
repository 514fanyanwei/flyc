package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusLineVersion;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.util.Ecode;

//中心下发线路信息版本
public class LZBusBuild0x49 extends LZBusBuildSendPacket {
private static final Log log = LogFactory.getLog(LZBusBuild0x49.class);
	
	public LZBusSendData sendData=new LZBusSendData();
	@Override
	public  byte[] buildSendPacket(JTReceiveData data){
		sendData.body=buildMessageBody(data,65531);  //用65531作为序列号
		byte[] bs=sendData.toByte();
		return bs;
	}
	
	@Override
	protected String buildMessageBody(Object content, int serialID) {
		LZBusLineVersion jt=(LZBusLineVersion)content;
		//获取数据表LINE_BASEINFO中线路的最新版本
		String lineVersion=Ecode.S2A(Global.getLineVersion(jt.lineNO));
		StringBuffer str = new StringBuffer();
		str.append(Ecode.DEC2HEX(serialID, 4));//下发指令的命令序列号
		str.append("49");  //信息类别
		str.append(Ecode.DEC2BCD(jt.lineNO)); //线路编号
		log.info(jt.lineNO+"线路最新版本号是:"+Global.getLineVersion(jt.lineNO));
		
		//要求保证所有线路都有版本号
		if((lineVersion!=null)&&(lineVersion.trim().length()!=0)){
			str.append(lineVersion);
		}else{
			//str.append("00000000000000");
			//返回终端现有的版本
			str.append(Ecode.S2A(jt.lineVersion));
		}
			
		
		//log.info("^^^^^"+str.toString());
		
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
