package cc.gps.active.lztaxi;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.lzbus.LZBusBuild0xb1;
import cc.gps.active.lzbus.LZBusBuild0xb2;
import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0801;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.parse.lzbus.LZBus0xa1;
import cc.gps.parse.lztaxi.LZTAXI0x4a;
import cc.gps.util.Ecode;

public class LZTAXIProcess0x4a extends LZTAXIProcessReceive{
	private static final Log log = LogFactory.getLog(LZTAXIProcess0x4a.class);
	
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		ByteBuf bf=inData.bb.duplicate();
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lztaxi004a")+":-------------");
		LZBusImage lzImage=Global.CID2BodyArray.get(inData.head.phone);
		if(lzImage==null){
			lzImage = new LZBusImage();
			lzImage.image=new String[50];
			for(int i=0;i<lzImage.image.length;i++) lzImage.image[i]=null;
			Global.CID2BodyArray.put(inData.head.phone, lzImage);
		}
		
		LZTAXI0x4a  lz=new LZTAXI0x4a(inData.bb,inData.head.phone,inData.head.len);
		LZBusImage saveImage=lz.convert();
		if(saveImage==null){
			log.debug(inData.head.phone+"切片不完整,继续组装");
			return null;
		}
		
		byte[]bs=null;
		//LZBusImage lzImage=Global.CID2BodyArray.get(inData.head.phone);
		String image[]=saveImage.image;
		
		
		log.debug(inData.head.phone+"切片完整,开始组装");
		
		StringBuffer imgBuf=new StringBuffer();
		for(int i=0;i<image.length;i++){
			if(image[i]!=null)imgBuf.append(image[i]);
			else break;
		}
		Global.CID2Body.remove(inData.head.phone);  //清除标志
		  
		JT0x0801 td = new JT0x0801();
		
		//以下五项必须(前四项通用)
		td.head=inData.head; //必须有
		//td.head.start=0x7e;//必须有
		//td.clientID=inData.clientID;  //必须有
		//td.head.messageID=0x0801;
		
		
		td.mecode=0;
		td.mid=1;
		
		//多媒体内容
		
		td.body=imgBuf.toString();
		td.data=imgBuf.toString();
		td.gps=null;
		td.result=0;//执行成功
		
		
		//多媒体事件信息

		td.mecode=0;//jpeg   //多媒体格式编码
		td.head.messageID=0x0801;  //转为JT/T-808　的图像应答

		//log.debug("多媒体数据内容："+td.data+"\n");
		//过滤
		//td.data=filtXOR(td.data); 
		
		//td.data="00000000"+td.data; //补8位，匹配交通部，gpsw中要去掉前8位

		
		//调用数据转发服务
		processTrans(td);  //调用父类方法统一转发
		/*
		//处理应答
		//对应答用户id
		int ptMessageID=Integer.parseInt(body.substring(0,4),16);
		//对应应答指令序列号
		int result = 0;  //0表示成功
		//处理发向终端的指令的应答情况
		String clientID=inData.clientID;
		processAnswer(ptMessageID,td.head.serialID,result,clientID);
		*/
		
		
		//写数据库
		write2DB(td);
		Global.CID2BodyArray.remove(inData.head.phone);
		//应答平台
		
		LZTAXIProcess0x53 ok=new LZTAXIProcess0x53();
		inData.bb=bf;
		ok.processMessageBody(inData);
		
		return null;
	}
		
	//------
	
	
	
}
