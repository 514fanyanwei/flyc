package cc.gps.active.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JT0x0800;
import cc.gps.data.jt808.JT0x0801;
import cc.gps.data.jt808.JTDataImage;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.parse.jt808.PJT0x0801;
import cc.gps.util.Ecode;

public class JTProcess0x0801 extends JTProcessReceive{
private static final Log log = LogFactory.getLog(JTProcess0x0801.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("0x0801")+":-------------");
		
		ByteBuf bff=inData.bb.duplicate();
		
		JTDataImage jt=(new PJT0x0801(inData)).convert();
		
		if(jt.unfinished.isEmpty()){
			//已采集完整
			JT0x0801 td = jt.jt0801;
			td.head=inData.head; //必须有
			td.gps.head.phone=inData.head.phone;
			log.info("终端上传的0X0801报文内容:"+td.mid+" * "+td.mtype+" * "+td.mecode+" * "+td.eventID+" * "+td.channelID+"\n");
			log.info("image:"+td.data);
			//调用数据转发服务
			processTrans(td);  //调用父类方法统一转发
			Global.JTCID2BodyArray.remove(inData.head.phone);
		}

		
		
		
		//向终端回送0x8800应答
		//JTBuild0x8800  jtb= new JTBuild0x8800();
		//byte[] bs=jtb.buildSendPacket(inData,jt.jt0801.mid,jt.unfinished);
		
		//向终端回送1个平台通用应答
		
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		byte[] bs=jtb.buildSendPacket(inData);
		return bs;
	
		/*
		//1  
		JT0x0801 td = new JT0x0801();
		//以下三项通用
		td.head=inData.head; //必须有
		td.start=0x7e;//必须有
		td.clientID=inData.clientID;  //必须有
		
		
		//多媒体事件信息
		td.mid=Long.valueOf(body.substring(0,8),16);             //多媒体数据ID
		td.mtype=Integer.valueOf(body.substring(8,10),16);  //多媒体类型
		log.info(td.mtype+"多媒体类型--------------------");
		td.mecode=Integer.valueOf(body.substring(10,12),16); //多媒体格式编码
		td.eventID=Integer.valueOf(body.substring(12,14),16);; //事件项编码
		td.channelID=Integer.valueOf(body.substring(14,16),16); //通道ID
		String gpsbody=body.substring(16, 72);
		log.info("gpsbody:"+gpsbody);
		//位置信息
		td.gps = new JT0x0200();
		td.gps.head=td.head;
		//td.gps.head.messageID=0x0200;
		
		td.gps.warning=gpsbody.substring(0,8);
		td.gps.status=gpsbody.substring(8,16);
		td.gps.iwarning=Integer.valueOf(td.gps.warning,16);
		td.gps.istatus=Integer.valueOf(td.gps.status,16);
		
		//log.info((String)gpsbody.subSequence(16, 24)+"------------");
		td.gps.latitude=Long.valueOf((String)gpsbody.substring(16, 24),16)/Math.pow(10, 6);
		//log.info((String)gpsbody.subSequence(24, 32)+"------------");
		td.gps.longitude=Long.valueOf((String)gpsbody.substring(24, 32),16)/Math.pow(10, 6);
		td.gps.altitude=Integer.valueOf((String)gpsbody.substring(32, 36),16);
		td.gps.velocity=Integer.valueOf((String)gpsbody.substring(36, 40),16);
		td.gps.orientation=Integer.valueOf((String)gpsbody.substring(40, 44),16);
		
		StringBuffer sb=new StringBuffer();
		sb.append(gpsbody.substring(44, 56));
		sb.insert(2, "-");
		sb.insert(5, "-");
		sb.insert(8, " ");
		sb.insert(11, ":");
		sb.insert(14, ":");
		td.gps.time=sb.toString();
		//多媒体内容
		td.data=body.substring(72);
		log.debug("终端上传的0x0801报文内容："+td.mid+"  "+td.mtype+"  "+td.mecode+" "+td.eventID+"  "+td.channelID+"\n");
		log.debug("位置信息"+td.gps.longitude+"  "+td.gps.latitude+" "+td.gps+"\n");
		log.debug("多媒体数据内容："+td.data+"\n");
		
		//调用数据转发服务
		processTrans(td);  //调用父类方法统一转发
	
		
		//向终端回送1个平台通用应答
		//JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8001);
		//向终端回送1个多媒体数据上传应答
		JTBuildSendPacket jtb=JTBuildSendPacketFactory.createBuild(0x8800);
		byte[] bs=jtb.buildSendPacket(td);
		
		//写数据库
		write2DB(td);
		return bs;*/
	}
}
