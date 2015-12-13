package cc.gps.active;

import cc.gps.active.jt808.JTBuildSendPacketFactory;
import cc.gps.active.lzbus.LZBusBuildSendPacketFactory;
import cc.gps.active.lztaxi.LZTAXIBuildSendPacketFactory;
//选用不同的设备
public class ProcessSendFactory {
	
	public static IBuildSendPacket createProcess(int ctid,int messageID)
	{
		IBuildSendPacket jtb=null;
		switch(ctid){
			case 1:jtb=JTBuildSendPacketFactory.createBuild(messageID);break;  //jt808
			case 2:jtb=LZBusBuildSendPacketFactory.createBuild(messageID);break;  //蓝斯公交
			case 3:jtb=LZTAXIBuildSendPacketFactory.createBuild(messageID);break;  //涪陵 蓝斯出租
		}
		return jtb;
	}
}
