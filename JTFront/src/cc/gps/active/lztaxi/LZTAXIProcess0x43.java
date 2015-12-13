package cc.gps.active.lztaxi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.lzbus.LZBusProcess0x41;
import cc.gps.active.lzbus.LZBusProcessReceive;
import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.parse.lzbus.LZBus0x41;
import cc.gps.parse.lztaxi.LZTAXI0x43;

public class LZTAXIProcess0x43 extends LZTAXIProcessReceive{
	private static final Log log = LogFactory.getLog(LZTAXIProcess0x43.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		log.debug("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus0041")+":-------------");
		JT0x0200 td=processData(inData);
		td.head=inData.head;
		
				
		//调用数据转发服务 
		if(((td.istatus&VStatus.NS[1])!=0)&&((td.istatus&VStatus.NS[7])==0)){//GPS有效 //非补传{
		//if((td.istatus&VStatus.NS[7])==0){ //非补传  //考虑到GPS无效时,也要采集车辆状态,特别是报警状态
			processTrans(td);  //调用父类方法统一转发
			Global.CID2POS.put(inData.head.phone, td);
			/*
			if((td.istatus&VStatus.NS[1])==1)//GPS有效
			{
				Global.CID2POS.put(inData.head.phone, td);
			}*/
		}
		
		//log.info(td);
		
		//写数据库
		write2DB(td);
		return null;
	}
	
	protected JT0x0200 processData(JTReceiveData inData){
		LZTAXI0x43 lz=new LZTAXI0x43(inData.bb);
		JT0x0200 td=lz.convert();
		JT0x0200 td0=Global.CID2POS.get(inData.head.phone); //取上一位置，用于纠偏
		
		//以下五项必须(前四项通用)
		td.head=inData.head; //必须有
		//log.info(td.head.phone.substring(1)+"***");
		if((td.istatus&VStatus.NS[1])==0){
			log.debug("未定位");
			//return null;
		}
		/*
		for(int i=0;i<32;i++){
			if((td.istatus&VStatus.NS[i])!=0) log.debug(VStatus.NSINFO[i]);
		}
		for(int i=0;i<32;i++){
			if((td.iwarning&VWarns.WS[i])!=0) log.debug(VWarns.WSINFO[i]);
		}*/
		/*
		if((td.istatus&VStatus.NS[7])==0){ //非补传数据
			//纠偏
			if((td0!=null)&&((td.istatus&VStatus.NS[1])==0)){
				double interval =DateUtil.DateDiff(td.time,td0.time,3);
				if(interval>0)
				   td=GPSRectify.driftAdjust(td0, td,interval);
			}
			Global.CID2POS.put(inData.head.phone, td);
		}*/
		//log.info(td);
		return td;
	}
}