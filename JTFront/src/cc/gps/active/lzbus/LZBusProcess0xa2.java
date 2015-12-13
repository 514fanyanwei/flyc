package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0801;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.util.Ecode;

public class LZBusProcess0xa2  extends LZBusProcessReceive{
	private static final Log log = LogFactory.getLog(LZBusProcess0x92.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		if(log.isDebugEnabled())
			log.info("处理来自"+inData.head.phone+"的"+Recourse.getOrder("lzbus00a2")+":-------------");
		
		byte[]bs=null;
		LZBusImage lzImage=Global.CID2BodyArray.get(inData.head.phone);
		String image[]=lzImage.image;
		boolean all=true;  //完整
		
		for(int i=0;i<image.length;i++){
			if(image[i]==null||image[i].equals("")){
				log.debug(inData.head.phone+"切片不完整,缺少第"+i+"个切片,指定终端重新上传");
				LZBusBuild0xb2 lzb=new LZBusBuild0xb2(lzImage.imgSerial,Ecode.DEC2HEX(i,4)); //下发指令
				bs=lzb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
				all=true;
				break;
			}
		}
		if(all){
			log.debug(inData.head.phone+"切片完整,开始组装");
			
			StringBuffer imgBuf=new StringBuffer();
			for(int i=0;i<image.length;i++){
				if(image[i]!=null)imgBuf.append(image[i]);
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
			//td.head.messageID=0x0801;  //转为JT/T-808　的图像应答
			td.head.messageID=0xa2;  // 不转为JT/T-808　的图像应答

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
			//---------
			LZBusBuild0xb1 lzb=new LZBusBuild0xb1(lzImage.imgSerial); //下发指令
			bs=lzb.buildSendPacket(inData); //bs为生成的可直接下发的指令，16进制byte[]
		}
		return bs;
	}
}