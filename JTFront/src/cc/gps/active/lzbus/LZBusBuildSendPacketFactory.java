package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LZBusBuildSendPacketFactory {
	//private static final Log log = LogFactory.getLog(LZBusBuildSendPacketFactory.class);
	public static LZBusBuildSendPacket createBuild(int messageID)
	{
		LZBusBuildSendPacket jbs=null;
		switch(messageID){
			case 0x01:jbs=new LZBusBuild0x01();break;  //中心命令应答
			case 0x41:jbs=new LZBusBuild0x41();break;  //心跳包
			case 0x9943:jbs=new LZBusBuild0x43();break;  //平台点名专用
			case 0x9949:jbs=new LZBusBuild0x49();break;  //下发最新线路信息版本
			case 0x9948:jbs=new LZBusBuild0x48();break;  //下发升级服务器信息
			case 0x9912:jbs=new LZBusBuild0x12();break;  //退出运营
			case 0x9911:jbs=new LZBusBuild0x11();break;  //加入运营
			//case 0x4F:break;
			//case 0x50:break;
			//case 0x51: jbs=new LSBuild0x51();break; //查询终端参数
			//case 0x43:break;
			//case 0x75:break;
			//case 0x4D:break;
			//case 0x41:break;
			//case 0x60:break;
			//case 0x61:break;
			//case 0x62:break;
			//case 0x63:break;
			//case 0x64:break;
			//case 0x65:break;
			//case 0x66:break;
			//case 0x7C:break;
			//case 0x7D:break;
			//case 0x7E:break;
			//case 0x7F:break;
			//case 0x01: jbs=new LSBusBuild0x01(); break; //登录应答
			//case 0x7B:break;
			//case 0x78: jbs = new LSBuild0x78();break;
			//case 0x81:break;
			//case 0x82:break;
			//case 0x83:break;
			//case 0xFE:break;
			default: jbs=new LZBusBuildSendPacket(messageID);break;  //默认用这个
		}
		return jbs;
		
	}

}