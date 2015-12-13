package cc.gps.active.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.lzbus.LZBusBuild0x41;
import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.JTSendData;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.parse.jt808.PJT0x0200;
import cc.gps.parse.lzbus.LZBus0x41;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;
//终端位置信息汇报
public class JTProcess0x0200 extends JTProcessReceive{
	private static final Log log = LogFactory.getLog(JTProcess0x0200.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0200")+":-------------");
		
		JT0x0200 td=(new PJT0x0200(inData.bb,false)).convert();
		JT0x0200 td0=Global.CID2POS.get(inData.head.phone); //取上一位置，用于纠偏和检查补传
		
		td.head=inData.head; //必须有
		
		//log.info(td);
		/*
		if((td.istatus&VStatus.NS[1])==0){
			log.debug("未定位");
		}
		String currentTime=DateUtil.GetDateTime();
		long ntime=DateUtil.DateDiff(currentTime,td.time, 3);
		//log.info(currentTime+" *******  "+td.time+"  ***** "+ntime);
		//log.info(Ecode.decToBin(td.istatus)+"*********");
		if(ntime>120){  //设2分钟后为补传
			Global.CID2POS.put(inData.head.phone, td);//缓存当前GPS位置 ,且于纠偏和检查是非补传
			td.istatus=td.istatus|VStatus.NS[7];
		}*/
		/*
		if(td0!=null){
		System.out.println("td.time:"+td.time+"   td0.time:"+td0.time);
		System.out.println(td.time.compareTo(td0.time));}*/
		if((td0==null)||(td.time.compareTo(td0.time)>0)){  //第1次收到或日期时间比上一次大
			Global.CID2POS.put(inData.head.phone, td);//缓存当前GPS位置 ,且于纠偏和检查是非补传
			//调用数据转发服务 
			//if(((td.istatus&VStatus.NS[1])!=0)&&((td.istatus&VStatus.NS[7])==0)){//GPS有效 //非补传{
			{ //非补传  //考虑到GPS无效时,也要采集车辆状态,特别是报警状态
				processTrans(td);  //调用父类方法统一转发
			}
		}else{
			td.istatus=td.istatus|VStatus.NS[7];//置补传标识
		}
		
		//写数据库
		write2DB(td);
		//向终端回送1个平台通用应答
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		return bs;
	}
}
