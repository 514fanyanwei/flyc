package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.lzbus.LZBusLogout;
import cc.gps.data.lzbus.LZBusOverSpeedTime;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;
import cc.gps.util.GPSRectify;
import cc.net.lzbus.GPSProcessVehicleHandler;

public class LZBusProcess0x7b extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0x53.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus007b")+":-------------");
		
		LZBusLogout logout=new LZBusLogout();
		JT0x0200 td=processData(inData);
		logout.pos=td;
		//调用数据转发服务 
		//if(((td.istatus&VStatus.NS[1])!=0)&&((td.istatus&VStatus.NS[7])==0)){//GPS有效 //非补传{
		if((td.istatus&VStatus.NS[7])==0) //非补传  //考虑到GPS无效时,也要采集车辆状态,特别是报警状态
		{
			processTrans(td);  //调用父类方法统一转发
			if((td.istatus&VStatus.NS[1])==1)//GPS有效
			{
				Global.CID2POS.put(inData.head.phone, td);
			}
		}
		//写数据库
		write2DB(td);
		log.debug("车机注销");
		//告知平台下线
		LZBusProcessDown lzp=new LZBusProcessDown();
		lzp.down(td.head.phone);
		
		return null;
		
		//-----------原版本
		/*
		JT0x0200 td0=Global.CID2POS.get(inData.clientID); //取上一位置，用于纠偏
		boolean old=false;  //补传数据标识 false 实时  true 补传数据
		//1  
		String body = inData.body;
		LZBusLogout logout=new LZBusLogout();
		//以下五项必须(前四项通用)
		JT0x0200 td=logout.pos;
		td.head=inData.head; //必须有
		td.start=0x7e;//必须有
		td.clientID=inData.clientID;  //必须有
		//td.clientID="015213605049";  //必须有，测试用
		td.head.phone=td.clientID;
		td.head.messageID=0x0200;
		
	    String latitude=Ecode.HEX2DEC(body.substring(4,6))+".";
		String la0=Ecode.HEX2DEC(body.substring(8,10));
		if(la0.length()<2) la0="0"+la0;
		String la1=Ecode.HEX2DEC(body.substring(10,12));
		if(la1.length()<2) la1="0"+la1;
		//String la=Ecode.HEX2DEC(body.substring(6,8))+"."+Ecode.HEX2DEC(body.substring(8,10))+Ecode.HEX2DEC(body.substring(10,12));
		String la=Ecode.HEX2DEC(body.substring(6,8))+"."+la0+la1;
		
		latitude+=(int)(Double.parseDouble(la)/60.0*Math.pow(10,8));
		
		String longitude=Ecode.HEX2DEC(body.substring(14,16))+".";
		
		String lon0=Ecode.HEX2DEC(body.substring(18,20));
		if(lon0.length()<2) lon0="0"+lon0;
		String lon1=Ecode.HEX2DEC(body.substring(20,22));
		if(lon1.length()<2) lon1="0"+lon1;
		
		String lon=Ecode.HEX2DEC(body.substring(16,18))+"."+lon0+lon1;
		longitude+=(int)(Double.parseDouble(lon)/60.0*Math.pow(10, 8));
		
		td.latitude=Double.parseDouble(latitude);
		td.longitude=Double.parseDouble(longitude);
		//log.info(body.substring(26,28));
		td.velocity=Integer.valueOf(Ecode.HEX2DEC(body.substring(26,28)));
		
		String ts=Ecode.HEX2DEC(body.substring(28,30))+Ecode.HEX2DEC(body.substring(30,32));
		
		String ori=Integer.valueOf(ts)+"."+Ecode.HEX2DEC(body.substring(32,34));
		//log.info(ori);
		
		td.orientation=Double.parseDouble(ori);
		
		//累计里程
		String mile=Ecode.HEX2DEC(body.substring(34,38))+"."+Ecode.HEX2DEC(body.substring(38,40));//累计里程
		td.mileage=Double.parseDouble(mile);
		
		//td.time=body.substring(40,42)+"-"+body.substring(42,44)+"-"+body.substring(44,46)+" "+body.substring(46,48)+":"+body.substring(48,50)+":"+body.substring(50,52);
		
		StringBuffer sb=new StringBuffer();
		sb.append(body.subSequence(40, 52));
		sb.insert(2, "-");
		sb.insert(5, "-");
		sb.insert(8, " ");
		sb.insert(11, ":");
		sb.insert(14, ":");
		//log.debug(sb.toString());
		
		td.time="20"+sb.toString();
		
		
		
		log.info("车机注销"+logout);
		//告知平台下线
		GPSProcessLZBusVehicleHandler.down(inData.clientID);
		
		if(old==false){ //非补传数据
			//纠偏
			
			if(td0!=null){
				double interval =DateUtil.DateDiff(td.time,td0.time,3);
				if(interval>0)
				   td=GPSRectify.driftAdjust(td0, td,interval);
			}
			
			//调用数据转发服务 
			processTrans(td);  //调用父类方法统一转发
			Global.CID2POS.put(inData.clientID, td);
		}
		
		//调用数据转发服务 
		processTrans(td);  //调用父类方法统一转发
				
		//写数据库
		write2DB(td);
		//return bs;
		return null;*/
	}
}