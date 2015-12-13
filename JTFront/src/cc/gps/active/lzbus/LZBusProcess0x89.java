package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.lzbus.LZBusDriver;
import cc.gps.data.lzbus.LZBusDriverRequest;
import cc.gps.data.lzbus.LZBusStation;
import cc.gps.parse.lzbus.LZBus0x41;
import cc.gps.parse.lzbus.LZBus0x89;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;
import cc.gps.util.GPSRectify;

public class LZBusProcess0x89  extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0x89.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		//if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus0089")+":-------------");
		LZBus0x89 lz=new LZBus0x89(inData.bb);
		LZBusDriver driver=lz.convert();
		if(Global.DriverSet.contains(driver.driverID)){
			JT0x0200 td=driver.pos;
			driver.head=inData.head;
			td.head.phone=inData.head.phone;
			
			Global.CID2Driver.put(inData.head.phone, driver.driverID);
			
			
			if((td.istatus&VStatus.NS[7])==0)//非补传
			{
				LZBusDriverRequest request=new LZBusDriverRequest();
				request.head=inData.head; //必须有
				request.head.start=0x02;//必须有
				//request.clientID=inData.clientID;  //必须有
				request.head.phone=inData.head.phone;
				request.head.messageID=0x89;  //当作 文本处理转发
				request.requestType="驾驶员签到";
				processTrans(request);
				if((td.istatus&VStatus.NS[1])==1)//GPS有效
				{ 
					//调用数据转发服务 
					processTrans(td);  //调用父类方法统一转发
				}
			}
	
			//写数据库
			driver.login=1;  //签到
			log.info(driver);
			write2DB(driver);
			log.info(driver.driverID+" 签 到");
		}else{
			log.info(driver.driverID+" 非法卡");
		}
		//向终端回送1个应答
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //应答
		//log.info(inData.head.serialID+"--------****"+inData.head.messageID);
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		return bs;
	}
	
}
