package cc.gps.active.lztaxi;

import cc.gps.active.lzbus.LZBusBuild0x01;
import cc.gps.active.lzbus.LZBusBuild0x11;
import cc.gps.active.lzbus.LZBusBuild0x12;
import cc.gps.active.lzbus.LZBusBuild0x41;
import cc.gps.active.lzbus.LZBusBuild0x43;
import cc.gps.active.lzbus.LZBusBuild0x49;
import cc.gps.active.lzbus.LZBusBuildSendPacket;

public class LZTAXIBuildSendPacketFactory {
	//private static final Log log = LogFactory.getLog(LZBusBuildSendPacketFactory.class);
	public static LZTAXIBuildSendPacket createBuild(int messageID)
	{
		LZTAXIBuildSendPacket jbs=null;
		switch(messageID){
			case 0x80:jbs=new LZTAXIBuild0x80();break;  //中心对登录命令的应答
			case 0x78:jbs=new LZTAXIBuild0x78();break;  //心跳包 在线保持
			/*
			case 0x9943:jbs=new LZBusBuild0x43();break;  //平台点名专用
			case 0x9949:jbs=new LZBusBuild0x49();break;  //线路信息版本一致的应答
			case 0x9912:jbs=new LZBusBuild0x12();break;  //退出运营
			case 0x9911:jbs=new LZBusBuild0x11();break;  //加入运营*/
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
			default: jbs=new LZTAXIBuildSendPacket(messageID);break;  //默认用这个
		}
		return jbs;
		
	}

}