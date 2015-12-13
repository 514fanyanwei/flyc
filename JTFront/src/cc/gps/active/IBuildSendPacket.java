package cc.gps.active;
//可删除
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

import java.nio.ByteBuffer;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Config;
import cc.gps.config.Global;
import cc.gps.data.SendData;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.JTSendData;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.data.lztaxi.LZTAXISendData;
import cc.gps.service.SocketSessionManager;
import cc.gps.util.Cache;
import cc.gps.util.Ecode;
/**
 * 该类负责向终端下发数据,包括两种下发：
 * 1.对终端发到服务器数据的回应，该类回应不需终端应答  byte[] buildSendPacket(JTReceiveData data)
 * 2.转发平台向终端下发的指令，需终端应答  boolean buildSendPacket(String clientID,Object content)
 */
public abstract class IBuildSendPacket {
private static final Log log = LogFactory.getLog(IBuildSendPacket.class);
	
	//仅用于生成回复终端的消息头   回复终端消息才需要重写
	protected  JTMessageHead buildMessageHead(JTMessageHead rhead){return null;}
	
	/**
	 * 仅用于接收到终端数据后回复终端，回复指令不需终端应答，供JTProxcessReceive类使用
	 * 流程：
	 * (1)构建消息头，JTMessageHead buildMessageHead(JTMessageHead rhead)
	 * (2)构建消息体, String buildMessageBody(Object content)
	 * (3)返回消息的16进制字节数组
	 */
	public  byte[] buildSendPacket(JTReceiveData data){ 
		return null;
	}
	
	
	/**
	 * 下发指令仅需调用此方法
	 * 用于向clientID下发指令,各下发指令的子类需重写，流程是：
	 * (1)重写buildMessageBody(content)，构造消息头和消息体
	 * (2)调用boolean send(String clientID,SendData sendData)方法，发送指令
	 */
	public abstract boolean buildSendPacket(String clientID,Object content,int serialID);//{ return false; }
	
	/** 
	 * 该方法负责为回应终端请求和平台下发指令构建发向终端的消息
	 * 近回结果是格式为16进制字符串 
	 * 若为回复终端            Object 强制转为JTReceiveData类型，该方法仅负责构造消息体，
	 * 						     消息头由JTMessageHead buildMessageHead(JTMessageHead rhead)组建
	 * 若为下发指令 　　Object 根据messageID需要转换为相应类型,该方法负责构造消息头和消息体
	 */
	protected abstract String buildMessageBody(Object content,int serialID);
	
	
	
	/**
	 * 用于平台统一向向终端下发指令，和与终端连接后重发缓存中的指令(JTProcessReceive中用)， 流程：
	 * (1)检查clientID是否有没回复指令，有则不能下发
	 * (2)缓存下发指令，用于验证终端对该指令的回复
	 * (3)从缓存的终端Session中，取出相应连接,其中session是否过期由SocketSessionManager统一管理
	 * (4)发送成功，返回真，失败返回假,但返回真并不意味终端一收到并执行，
	   * 　　　　只有当终端返回对该指令的应答才说明终端收到并执行,
	 * 
	 * @param clientID
	 * @param sendData
	 * @return
	 */
	
	//final public static boolean send(String clientID,SendData sendData,boolean repeated){  
	final public static boolean send(String clientID,SendData sendData){
		boolean isSuccess=false;
		/*
		if(repeated!=true){
				if(!beforeSend(clientID)){ log.info(clientID+"有未回复指令"); return false;}
		}*/
		long mid=sendData.head.messageID;
		int count=sendData.getSendCount();
		sendData.setSendCount(++count);
		//缓存发送指令，用于检测验证接收端回复 
		//Global.orderAnswer.put(clientID, sendData);
		byte[] bs=null;
		
		if(sendData instanceof JTSendData){
			//log.info("---------------------");
			bs=((JTSendData)sendData).toByte();
		}
			
		if(sendData instanceof LZBusSendData){
			//log.info("**********************");
			bs=((LZBusSendData)sendData).toByte();
		}
		if(sendData instanceof LZTAXISendData){
			//log.info("**********************");
			bs=((LZTAXISendData)sendData).toByte();
		}
		
		
		log.info("平台拟向终端:"+clientID+"发送的报文是:"+Ecode.ByteArray2HexString(bs, -1, -1));
		
		
		//Session是否过期在getContent方法中已处理，若过期则返回null
		Cache cache=(Cache)SocketSessionManager.getContent(clientID);
		if(cache!=null){
			ChannelHandlerContext ctx=(ChannelHandlerContext)(cache.getValue());
			try{
				if(ctx!=null){
					//发送数据
					ByteBuf bb=Unpooled.buffer(bs.length);
					bb.writeBytes(bs);
					ctx.writeAndFlush(bb);
					isSuccess=true;
					//写日志文件
					//String messageID=Ecode.DEC2HEX(bs[5],4);
					//System.out.println(Ecode.DEC2HEX(mid,4)+"*********||||||");
					String messageID=Ecode.DEC2HEX(mid,4);
					save2Queue(messageID,clientID,bs);
				}
			}catch(Exception e){  //该异常可能不会发生，为保万一而设置
				log.error("终端连接超时，或不在线");
				SocketSessionManager.putContent(clientID, null, Integer.parseInt(Config.getKey("clientSessionExpired")));
				e.printStackTrace();
			}
		}else{
			log.error("终端连接超时，或不在线 cache:"+cache);
			//saveSendFailOrder(sendData);
		}
		
		return isSuccess;
	}
	
	
	protected  String buildMessageExtral(JTReceiveData data){return null;}
	protected  void buildTransData(JTReceiveData data) {}
	
	//下发指令前检查是否以前下发的指令还没回复,若没回复,不能下发新指令
	/*
	protected static boolean beforeSend(String clientID){  //未回复,则返回false
		if(Global.orderAnswer.containsKey(clientID)){
			SendData sd=Global.orderAnswer.get(clientID);
			// 在JTPRocessReceive刚建立连接 **也**检查重发次数
			if(sd.getSendCount()>Integer.parseInt(Config.getKey("sendCount"))) {
				Global.orderAnswer.remove(clientID); //超过发送次数，停止发送
				log.info("发向"+clientID+"的报文超过重发次数");
				return true;
			}else
			return sd.isAnswer();
		}else return true;
	}*/

	 //写入日志文件
	/*
	protected static void save2Queue(byte[] bs){
			if(bs==null) return ;
			StringBuffer str= new StringBuffer();
			str.append("LZBUS|");
			str.append((new Date()).toLocaleString()+"	S ");
			
			for(int i=0;i<bs.length;i++){
				str.append(Ecode.DEC2HEX(bs[i], 2));
			}
			str.append("\n");
			Global.logData.add(str.toString());
			//有变化就写日志，性能不高，正式用时删除
			Global.logData.trigger();
		}*/
	protected static void save2Queue(String messageID,String clientID,byte[] bs){
		if(bs==null) return ;
		//if(clientID.equals("")) return;
		//if(!clientID.equals("013368260872")) return;
		//下发报文写GPSPacketLog表
		String ctype=null;
		switch(bs[0]){
			case 0x02:ctype="LENZ"; break;
			case 0x7e:ctype="JT808"; break;
		}
		StringBuffer str= new StringBuffer();
		for(int i=0;i<bs.length;i++){
			str.append(Ecode.DEC2HEX(bs[i], 2));
		}
		
		Global.add(ctype, "S", messageID, clientID, str.toString());
		
		//下发报文写文本文件
		/*
		StringBuffer str= new StringBuffer();
		switch(bs[0]){
			case 0x02:str.append("LZBUS|"); break;
			case 0x7e:str.append("JT808");break;
		}
		str.append((new Date()).toLocaleString()+" | S"+" | ");
		str.append(messageID+" | "+clientID+" | ");
		for(int i=0;i<bs.length;i++){
			str.append(Ecode.DEC2HEX(bs[i], 2));
		}
		str.append("\n");
		Global.logData.add(str.toString());
		//有变化就写日志，性能不高，正式用时删除
		Global.logData.trigger();*/
	}
	/*要考虑不同设备的下发指令
	private static void saveSendFailOrder(SendData obj){
		
		LZBusSendData data=(LZBusSendData)obj;
		C2XAnswer td = new C2XAnswer();
		
		//处理发向终端的指令的应答情况
		String clientID=data.head.phone;
		

		td.head= new JTMessageHead();//)data.head.clone(); //必须有
		td.head.phone=clientID;
		//td.clientID=clientID; //必须有
		
		td.messageID=data.head.messageID;  
		td.head.messageID=data.head.messageID;
		td.result=1;  //失败
		//记录下发的消息内容
		td.attachment=new JTReceiveData();
		td.attachment.body=Ecode.ByteArray2HexString(data.toByte(), -1, -1);

		//存数据库
		Task task1= new Save2DBTask(td);
		Global.tp.addTask(task1);
	}*/

}
