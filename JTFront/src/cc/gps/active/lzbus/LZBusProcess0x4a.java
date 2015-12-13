package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.lzbus.LZBusDriverRequest;
import cc.gps.data.lzbus.LZBusStation;
import cc.gps.parse.lzbus.LZBus0x41;
import cc.gps.parse.lzbus.LZBus0x4a;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;
import cc.gps.util.GPSRectify;

public class LZBusProcess0x4a  extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0x4a.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		log.debug("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus004a")+":-------------");
		
		JT0x0200 td=processData(inData);
		LZBusDriverRequest request=new LZBusDriverRequest();
		int len=inData.head.len-3-35;
		LZBus0x4a lz=new LZBus0x4a(inData.bb,len);
		request=(LZBusDriverRequest)(lz.convert());
		
		td.head=inData.head;//必须有
		request.head=inData.head; //必须有
		request.head.messageID=0x72;  //当作 文本处理转发
		request.pos=td;
        
		//非营运类型
		//log.info(request.requestType+"   "+request.stationID);
		//向终端回送1个应答
		inData.head.messageID=0x4a;
		//LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x12); //应答
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //通用应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
				
		//调用数据转发服务 
		if((td.istatus&VStatus.NS[7])==0)//非补传
		{
			processTrans(request);//终端信息转发
			processTrans(td);  //位置信息 转发    调用父类方法统一转发
			if((td.istatus&VStatus.NS[1])==1)//GPS有效
			{
				Global.CID2POS.put(inData.head.phone, td);
			 }
		}
		//log.info(request);
		//写数据库
		write2DB(request);
		
		
		return bs;
	}
	
	public static void main(String args[]){
		//log.info(Ecode.binToDec("110000000000011")|VStatus.NS[15]);
		log.info(2&1);
	}
}
