package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.lzbus.LZBusDriverRequest;
import cc.gps.parse.lzbus.LZBus0x4a;
import cc.gps.parse.lzbus.LZBus0x4b;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;
import cc.gps.util.GPSRectify;

public class LZBusProcess0x4b  extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0x4a.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus004a")+":-------------");
		
		JT0x0200 td=processData(inData);
		LZBusDriverRequest request=new LZBusDriverRequest();
		
		LZBus0x4b lz=new LZBus0x4b(inData.bb);
		request=(LZBusDriverRequest)(lz.convert());
		
		
		td.head=inData.head;//必须有
		request.head=inData.head; //必须有
		request.head.messageID=0x72;  //当作 文本处理转发
		request.pos=td;
		
		
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
		
		//向终端回送1个应答
		inData.head.messageID=0x4b;
		LZBusBuildSendPacket jtb=LZBusBuildSendPacketFactory.createBuild(0x01); //应答
		byte[] bs=jtb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		
		return bs;
		
		
		//以下为原始版本
		/*
		JT0x0200 td0=Global.CID2POS.get(inData.clientID); //取上一位置，用于纠偏
		//1  
		String body = inData.body;
		LZBusDriverRequest request=new LZBusDriverRequest();
		//以下五项必须(前四项通用)
		JT0x0200 td=request.pos;
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
		
		String s=body.substring(52, 60);  //报警信息
		//log.info(body.substring(60,64));
		String before=Ecode.HEX2DEC(body.substring(60,64));//站点编号
		
		
		request.stationID=Integer.parseInt(before);//String extID=Integer.parseInt(before);
		
		
		//站间里程
		mile=Ecode.HEX2DEC(body.substring(64,68))+"."+Ecode.HEX2DEC(body.substring(68,70));//站间里程
		request.sInterval=Double.parseDouble(mile);
		
		//非营运类型编号
		request.id=Integer.parseInt(Ecode.HEX2DEC(body.substring(70,78)));
		
		//行驶里程
		mile=Ecode.HEX2DEC(body.substring(80,82))+Ecode.HEX2DEC(body.substring(78,80));//行驶里程 小端模式(米)
		request.fmile=Double.parseDouble(mile)/1000.0;

		s=Ecode.HEX2DECLH(s);
		String bin=Ecode.decToBin(Long.parseLong(s));
		
		while(bin.length()<32){
			bin="0"+bin;
		}
		log.debug("bin:"+bin);
		StringBuffer st=new StringBuffer("00000000000000000000000000000000");
		StringBuffer wa=new StringBuffer("00000000000000000000000000000000");
		boolean old=false;  //补传数据标识 false 实时  true 补传数据
		for(int i=0;i<bin.length();i++){
			switch(i){
				case 0:
					if(bin.charAt(31-i)=='1') st.replace(31, 32, "1"); break;  //ok ACC     
				case 1:
					if(bin.charAt(31-i)=='1') wa.replace(30, 31, "1"); break;  //超速
				case 2:
					 break;
				case 3:
					if(bin.charAt(31-i)=='1') wa.replace(12, 13, "1"); break;  //停留超时　
				case 4:
					if(bin.charAt(31-i)=='1') wa.replace(31, 32, "1"); break;  //用户按键 重要  ok
				case 5:
					if(bin.charAt(31-i)=='1') wa.replace(25, 26, "1"); break;
				case 6:
					if(bin.charAt(31-i)=='1') wa.replace(26, 27, "1"); break;
				case 7:
					if(bin.charAt(31-i)=='1') wa.replace(27, 28, "1"); break;
				case 8:
					break;
				case 9:
					if(bin.charAt(31-i)=='1') st.replace(30, 31, "1"); break;  //未定位　GPS无效  ok
				case 10:
					if(bin.charAt(31-i)=='1') st.replace(18, 19, "1"); break;  //1 营运中
				case 11:
					if(bin.charAt(31-i)=='1') st.replace(17, 18, "1"); break;  //1 上行
				case 12:
					break;
				case 13:
					 break;
				case 14:
					break;
				case 15:
					 break;
				case 16:
					break;
				case 17:
					break;
				case 18:
					break;
				case 19:
					if(bin.charAt(31-i)=='1') st.replace(25, 26, "1"); break;  //重车 1  空车 0   st 6位
				case 20:
					if(bin.charAt(31-i)=='1') wa.replace(8, 9, "1"); break;  //越界报警
				case 21:
					break;
				case 22:
					break;
				case 23:
					if(bin.charAt(31-i)=='1'){ st.replace(24, 25, "1");old=true;} break;  //补传GPS数据    ok  //st 7位
				case 24:
					break;
				case 25:
					break;
				case 26:
					break;
				case 27:
					break;
				case 28:
					if(bin.charAt(31-i)=='1') wa.replace(29, 30, "1"); break;  //疲劳驾驶
				case 29:
					break;
				case 30:
					if(bin.charAt(31-i)=='1') st.replace(20, 21, "1"); break;  //车辆断电
				case 31:
					//if(bin.charAt(31-i)=='1') status.replace(31, 32, "1"); break;
			}
		}
		td.status=Ecode.binaryString2hexString(st.toString());
		td.warning=Ecode.binaryString2hexString(wa.toString());
		td.iwarning=Long.valueOf(td.warning,16);
		td.istatus=Long.valueOf(td.status,16);
		
		
		
		log.info(request);
		
		
		if(old==false){ //非补传数据
			//纠偏
			double interval =DateUtil.DateDiff(td.time,td0.time,3);
			if(interval>0)
			   td=GPSRectify.driftAdjust(td0, td,interval);
			
			//调用数据转发服务 
			processTrans(td);  //调用父类方法统一转发
			Global.CID2POS.put(inData.clientID, td);
		}
		
		 
				
		//写数据库
		write2DB(td);
		//return bs;
		return null;*/
	}
}