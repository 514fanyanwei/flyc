package cc.gps.thread;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.IBuildSendPacket;
import cc.gps.active.ProcessSendFactory;
import cc.gps.config.Global;
import cc.gps.data.SendData;
import cc.gps.data.X2CData;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.util.Ecode;

public class SendOrderTask  extends Task{
 
	private static final Log log = LogFactory.getLog(SendOrderTask.class);
	//private LinkedList<X2CData> list;
	private X2CData data;
	/*
	public SendOrderTask(LinkedList<X2CData> data){
		this.list=data;
	}*/
	public SendOrderTask(X2CData data){
		this.data=data;
	}
	
	public  void send(){

		if(data==null) return;
		int messageID=data.getMessageID();
		int ctid=data.getCtid();
		//log.info("收到Web发向终端的报文ID:"+Integer.toHexString(messageID));		
		log.info("收到Web发向终端:"+data.getClientID()+"的报文ID:0x"+Integer.toHexString(messageID)+"  设备类型:"+ctid);
		IBuildSendPacket jtb=ProcessSendFactory.createProcess(ctid, messageID);
		//boolean isSend=jtb.buildSendPacket(data.getClientID(),data.getContent(),data.getSerialID()); //返回下发终端结果
		//交由相应设备的报文组装及下发程序处理
		jtb.buildSendPacket(data.getClientID(),data.getContent(),data.getSerialID());
		data.setMilisSendTime(new Date().getTime()); //记录发送时间
		//System.out.println(data.getClientID()+"**"+data.getSerialID());
		Global.orderSended.put(data.getClientID()+data.getSerialID(),data);
	}
	
	
	//以下未用
	/*
	public void run(){
		log.info("接收到来自Web平台的报文");
		
		 // 包含两种:
		 //1 连接请求  messageID 
		 //2 平台心跳
		 //3 平台对前端的应答
		 //4 下发指令,根据终端ID从缓存中找到与终端关联的Session,通过该session下发数据
		 // 
		 //应含:
		 //  (1)messageID 指令类型
		 //  (2)clientID  终端ID
		 //  (3)content   内容  ,不同指令,该字段的类型不同
		 //  
		 
		if(data==null) return;
		int messageID=data.getMessageID();
		int ctid=data.getCtid();
		log.info("收到Web发向终端的报文ID:"+Integer.toHexString(messageID)+"\n设备类型:"+ctid);
		
		//交给JBuildSendPacketFactory工厂 负责发送到终端
		IBuildSendPacket jtb=ProcessSendFactory.createProcess(ctid, messageID);
		//IBuildSendPacket jtb=ProcessSendFactory.createProcess(2, messageID);
		
		//boolean isSend=jtb.buildSendPacket(data.getClientID(),data.getContent(),data.getSerialID()); //返回下发终端结果
		jtb.buildSendPacket(data.getClientID(),data.getContent(),data.getSerialID()); 
	//	if(isSend){//已发送,等待应答
		data.setMilisSendTime(new Date().getTime()); //记录发送时间
		//log.info("*********"+data.getClientID()+data.getSerialID());
		//缓存发送指令，用于检测验证接收端回复 
		Global.orderSended.put(data.getClientID()+data.getSerialID(),data);
	}*/

	@Override
	public Task[] taskCore() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean useDb() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean needExecuteImmediate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}

}
