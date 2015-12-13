package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.jt808.VWarns;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;
import cc.gps.util.GPSRectify;

public class LZBusProcess0x49 extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0x49.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus0049")+":-------------");
		
		JT0x0200 td=processData(inData);
		//调用数据转发服务 
		//if(((td.istatus&VStatus.NS[1])!=0)&&((td.istatus&VStatus.NS[7])==0)){//GPS有效 //非补传{
		if((td.istatus&VStatus.NS[7])==0) //非补传  //考虑到GPS无效时,也要采集车辆状态,特别是报警状态
		{
			//processTrans(td);  //调用父类方法统一转发
			if((td.istatus&VStatus.NS[1])==1)//GPS有效
			{
				Global.CID2POS.put(inData.head.phone, td);
			}
		}
		//写数据库
		write2DB(td);
		//向终端回送1个应答
		inData.head.messageID=0x49;
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //终端登录应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		return bs;
		
	}
}