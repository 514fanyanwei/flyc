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
import cc.gps.parse.lzbus.LZBus0x8b;
import cc.gps.util.Ecode;

public class LZBusProcess0x8b  extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0x8b.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		log.debug("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus008b")+":-------------");

		LZBus0x8b lz=new LZBus0x8b(inData.bb);
		LZBusDriverRequest driver=lz.convert();
		JT0x0200 td=driver.pos;
		driver.head=inData.head;
		td.head.phone=inData.head.phone;
		
		/*
		String body = inData.body;
		JT0x0200 td=processData(inData);
		String before=Ecode.HEX2DEC(body.substring(60,64));//站点编号
		LZBusDriverRequest driver=new LZBusDriverRequest();
		driver.head=inData.head; //必须有
		driver.head.start=0x02;//必须有
		driver.head.phone=inData.head.phone;
		driver.head.messageID=0x72;  //当作 文本处理转发
		*/
		
		/*
		driver.pos=td;
		
		driver.stationID=Integer.parseInt(before);//String extID=Integer.parseInt(before);
		
		//站间里程
		//站间里程
		String intervalMile=Ecode.HEX2DEC(body.substring(64,68))+"."+Ecode.HEX2DEC(body.substring(68,70));//站间里程
		driver.sInterval=Double.parseDouble(intervalMile);
		*/
		//上下行标志
		//driver.driverID=Ecode.A2S(body.substring(70,72));
		
		//发车或进入终点 改为  发车 1  表示进入营运状态   进入终点 2  表示非营运状态
		LZBusBuildSendPacket jtb=null;
		//inData.head.messageID=0x8b;
		if(driver.requestType.equals("转入营运")){
			jtb=LZBusBuildSendPacketFactory.createBuild(0x9911); //应答
		}
		if(driver.requestType.equals("转入非营运")){
			//driver.requestType="转入非营运";
			jtb=LZBusBuildSendPacketFactory.createBuild(0x9912); //应答
		}
		
		
		if((td.istatus&VStatus.NS[7])==0)//非补传
		{
			//调用数据转发服务  转发文本信息
			processTrans(driver);  //调用父类方法统一转发
			if((td.istatus&VStatus.NS[1])==1)//GPS有效  
			//考虑到GPS无效时,也要采集车辆状态,特别是报警状态  ?? 
			{
				//调用数据转发服务  转发GPS位置信息 
				processTrans(td);  //调用父类方法统一转发
			}
		}
		
		//写数据库
		write2DB(driver);
		
		//向终端回送1个应答
		//log.info(driver+"|||");
		LZBusBuildSendPacket jtb0=LZBusBuildSendPacketFactory.createBuild(0x01); //终端登录应答
		byte[] bs0=jtb0.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		/*
		for(int i=0;i<bs0.length;i++)
			System.out.print(bs0[i]+"|");
		System.out.println();*/
		//向终端回 改状态
		byte[] bs1=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		/*
		for(int i=0;i<bs1.length;i++)
			System.out.print(bs1[i]+"|");
		System.out.println();*/
		
		byte[]bs=new byte[bs0.length+bs1.length];
		int i=0;
		for(;i<bs0.length;i++)
			bs[i]=bs0[i];
		for(;i<bs.length;i++)
			bs[i]=bs1[i-bs0.length];
		/*
		for( i=0;i<bs.length;i++)
			System.out.print(Ecode.DEC2HEX(bs[i],2)+"|");
		System.out.println();*/
		return bs;
	}
}
