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
import cc.gps.parse.lzbus.LZBus0x89;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;
import cc.gps.util.GPSRectify;

public class LZBusProcess0x8a   extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0x8a.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		//if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus008a")+":-------------");

		
		LZBus0x89 lz=new LZBus0x89(inData.bb);  //because the 0x89's content with 0x8a is same
		LZBusDriver driver=lz.convert();
		if(Global.DriverSet.contains(driver.driverID)){
			JT0x0200 td=driver.pos;
			td.head.phone=inData.head.phone;
			driver.head=inData.head;
			/*
			String body = inData.body;
			JT0x0200 td=processData(inData);
			String before=Ecode.HEX2DEC(body.substring(60,64));//站点编号
			LZBusDriver driver=new LZBusDriver();
			driver.pos=td;
			
			driver.stationID=Integer.parseInt(before);//String extID=Integer.parseInt(before);
			
			//清空缓存司机
			Global.CID2Driver.put(inData.head.phone, null);
			//站间里程
			//站间里程
			String intervalMile=Ecode.HEX2DEC(body.substring(64,68))+"."+Ecode.HEX2DEC(body.substring(68,70));//站间里程
			driver.sInterval=Double.parseDouble(intervalMile);
			
			//司机编号
			driver.driverID=Ecode.A2S(body.substring(70,90));
			//司机psw
			driver.psw=Ecode.A2S(body.substring(90,110));
	
			
			*/
			//log.info(driver);
			if((td.istatus&VStatus.NS[7])==0)//非补传
			{
				LZBusDriverRequest request=new LZBusDriverRequest();
				request.head=inData.head; //必须有
				request.head.start=0x02;//必须有
				//request.clientID=inData.clientID;  //必须有
				request.head.phone=inData.head.phone;
				//request.head.messageID=0x89;  //当作 文本处理转发
				request.requestType="驾驶员签退";
				processTrans(request);
				if((td.istatus&VStatus.NS[1])==1)//GPS有效
				//考虑到GPS无效时,也要采集车辆状态,特别是报警状态  ??
				{ 
					//调用数据转发服务 
					processTrans(td);  //调用父类方法统一转发
				}
				//清空缓存司机
				Global.CID2Driver.put(inData.head.phone, null);
			}
			//写数据库
			
			driver.login=0; //签退
			write2DB(driver);
			log.info(driver.driverID+" 签 退");
		}else{
			log.info(driver.driverID+" 非法卡");
		}
		//向终端回送1个应答
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		return bs;
	}
}