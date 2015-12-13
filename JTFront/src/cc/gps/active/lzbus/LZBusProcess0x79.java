package cc.gps.active.lzbus;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.lzbus.LZBusStation;
import cc.gps.parse.lzbus.LZBus0x79;
import cc.gps.util.Ecode;

public class LZBusProcess0x79  extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0x79.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus0079")+":-------------");
		String body = inData.body;
		ByteBuf bb=inData.bb.duplicate();
		JT0x0200 td=processData(inData);
		
		LZBus0x79 lz= new LZBus0x79(bb);
		LZBusStation st=lz.convert();
		
		st.head=inData.head;
		st.pos=td;
		
		//向终端回送1个应答
		inData.head.messageID=0x79;
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
			
		
		
		if(((td.istatus&VStatus.NS[1])!=0)&&((td.istatus&VStatus.NS[7])==0))//GPS有效 //非补传
		{
			//调用数据转发服务 
			processTrans(td);  //调用父类方法统一转发
			//写数据库
			write2DB(st);
		}
		//log.debug(st);
		return bs;
	}
}
