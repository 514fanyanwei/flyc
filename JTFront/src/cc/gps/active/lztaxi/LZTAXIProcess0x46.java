package cc.gps.active.lztaxi;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.lzbus.LZBusBuildSendPacket;
import cc.gps.active.lzbus.LZBusBuildSendPacketFactory;
import cc.gps.active.lzbus.LZBusProcess0x41;
import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.lzbus.LZBusStation;
import cc.gps.data.lztaxi.LZTAXIStation;
import cc.gps.parse.lzbus.LZBus0x7e;
import cc.gps.parse.lztaxi.LZTAXI0x43;
import cc.gps.parse.lztaxi.LZTAXI0x46;

public class LZTAXIProcess0x46 extends LZTAXIProcess0x43{
	private static final Log log = LogFactory.getLog(LZTAXIProcess0x46.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		//if(log.isDebugEnabled())
		log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lztaxi0046")+":-------------");

		//ByteBuf bb=inData.bb.duplicate();
		JT0x0200 td=processData(inData);
		LZTAXI0x46 lz= new LZTAXI0x46(inData.bb);
		LZTAXIStation st=lz.convert();
		
		st.head=inData.head;
		td.head.phone=inData.head.phone;
		st.pos=td;
		
		if(inData.head.messageID==0x46) st.inout=0;//进入
		if(inData.head.messageID==0x4f) st.inout=1;//离开
		
		
		/*
		//向终端回送1个应答
		inData.head.messageID=0x7e;
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		
		if(((td.istatus&VStatus.NS[1])!=0)&&((td.istatus&VStatus.NS[7])==0))//GPS有效 //非补传
		{
			//调用数据转发服务 
			processTrans(td);  //调用父类方法统一转发
			//写数据库
			write2DB(st);
		}*/
		log.info(st);
	
		
		//写数据库
		write2DB(td);
		return null;
	}
}