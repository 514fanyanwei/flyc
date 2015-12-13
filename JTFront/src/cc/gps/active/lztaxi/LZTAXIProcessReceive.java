package cc.gps.active.lztaxi;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.active.IProcessReceive;
import cc.gps.config.Config;
import cc.gps.config.Global;
import cc.gps.data.SendData;
import cc.gps.data.X2CData;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.service.MonitorSessionManager;
import cc.gps.service.SocketSessionManager;
import cc.gps.thread.Save2DBTask;
import cc.gps.thread.SaveObjTask;
import cc.gps.thread.SaveWarnTask;
import cc.gps.thread.Task;
import cc.gps.util.Cache;
import cc.gps.util.Ecode;
import cc.gps.util.SerializeUtil;

public class LZTAXIProcessReceive implements IProcessReceive{
	private static final Log log = LogFactory.getLog(LZTAXIProcessReceive.class);
	private String clientID="";
	private int start=0;
	@Override
	/**
	 *  消息处理流程：
	 * 1.List<JTReceiveData> processMessageHead(Buffer buffer) 处理消息，区分出消息头 和消息体部分并保存到List<ReciveData>中
	 *   (考虑buffer中可能包含多个数据)
	 * 2.根据messageID选用不同的处理方案  处理消息体
	 *   (1)首次连接（终端鉴权(0x4C)时），保存与终端连接的session(注意　session的生命期),重发发往该终端的命令，并记重发次数
	 *   (2)通过消息体处理器工厂LSProcessBodyFactory寻找相应的消息体处理器
	 *   (3)消息体处理器返回服务器向终端的应答，(若为终端心跳，则刷新session，考虑session的生命期与终端心跳一致或略大)
	 *   (4)同时，向监控平台发送数据,void processMonitor(List<JTReceiveData> datas)
	 */
	public final SendData processData(ByteBuf bb,ChannelHandlerContext ctx) {
		ByteBuf savebb=bb.duplicate();
		//写数据表GPSPacketLog,保存收到的原始报文
    	Attribute<String> attr = ctx.channel().attr(Global.CLIENTID);
		String clientID=attr.get();
    	StringBuffer str= new StringBuffer();
		while(savebb.isReadable()){
			str.append(Ecode.DEC2HEX(savebb.readUnsignedByte(),2));
		}
		Global.add("LZTAXI", "R", "00"+str.substring(6,8), clientID, str.toString());
		
		//开始处理报文
		if(clientID!=null){
			Global.CID2ONOFF.put(clientID, 1);
		}
		//1 处理接收的消息头
		List<JTReceiveData> datas=processMessageHead(bb,clientID);//***********
		List<Byte> list = new LinkedList<Byte>();
		if((datas==null)||datas.size()==0){
			return null;
		}
		//Global.CID2END.put(clientID,0);//该终端所收报文完整
		//2 根据messageID选用不同的处理方案  处理消息体
		for(Iterator<JTReceiveData> it=datas.iterator();it.hasNext();){
			JTReceiveData jtrdata=(JTReceiveData)it.next();
			//仅在第1次建立连接（终端鉴权(0x0102)时）或检测到终端心跳时，保存与终端连接的session
			
			//供平台下发数据用
			//0x75 login   0x45 hold
			//if((jtrdata.head.messageID==0x75)||(jtrdata.head.messageID==0x45))
			{
				if(jtrdata.head.messageID==0x4c){
					//现将clientID设为sim
					ByteBuf bf=jtrdata.bb.duplicate();
					bf.skipBytes(4);
					clientID="";
					for(int i=0;i<6;i++){
						int n=bf.readUnsignedByte();
						if(n<10) clientID+="0"+n;
						else clientID+=n;
					}
					//标记context
					attr.set(clientID);
				}
				
				clientID=attr.get();
				//saveSessionCache(clientID, ctx, Integer.parseInt(Config.getKey("clientSessionExpired")));
			}
			log.info("收到"+clientID+"发来的报文ID:0x"+Integer.toHexString(jtrdata.head.messageID));
			saveSessionCache(clientID, ctx, Integer.parseInt(Config.getKey("clientSessionExpired")));
			jtrdata.head.phone=clientID;

			//构造消息体处理器
			//log.info("0x"+Ecode.DEC2HEX(jtrdata.head.messageID)+"******");
			LZTAXIProcessReceive process=LZTAXIProcessReceiveBodyFactory.createProcess(jtrdata.head.messageID);
			/*
			ByteBuf bff=jtrdata.bb.duplicate();
			while(bff.isReadable()){
				log.info(Ecode.DEC2HEX(bff.readUnsignedByte())+"|");
			}log.info("");*/
			if(jtrdata==null)log.info("***********************************");
			//if(process==null) return null;
			byte[] bs=process.processMessageBody(jtrdata);
			
			if(bs!=null){
				for(int i=0;i<bs.length;i++){
					list.add(bs[i]);
				}
			}
			
			//写队列，准备写日志文件
			// 2014-5-21关闭写文本文件
			//save2Queue(Ecode.DEC2HEX(jtrdata.head.messageID,4),jtrdata.head.phone,savebb);
			
			//向GUI写入
			//CCGPSF.jTextArea1.insert(new Date().toLocaleString()+" | "+clientID+" | "+Ecode.DEC2HEX(savebb.getUnsignedByte(3),2)+" | "+savebb.toString()+"\n",0);
		}
		SendData sendData = null;
		if(list.size()>0){
			sendData=new SendData();
			byte[] send =new byte[list.size()];
			int i=0;
			for(Iterator<Byte> it=list.iterator();it.hasNext();){
				send[i++]=it.next();
			}
			sendData.setClientID(clientID);
			sendData.setBytes(send);
			sendData.setAnswer(true); //对终端请求的回应不需要终端应答
		}
		//向监控平台发
		processMonitor(datas);
		
		return sendData;
	}
	


		

	/**
	 * 处理发往终端clientID的消息为messageID,流水号为serialID的指令的应答情况 
	 */
	//待验证****************
	
	final protected void processAnswer(int messageID,int serialID,int result,String clientID){
		
		log.debug("收到终端"+clientID+"对流水号:"+serialID+"的应答");
		log.info("应发送待回应指令数量:"+Global.orderSended.size());
		String key=clientID+serialID;
		if(Global.orderSended.get(key)!=null){
			X2CData data=Global.orderSended.remove(key);
			log.info(data.getClientID()+" "+data.getContent());
			//log.info("size2  "+Global.orderSended.size());
			log.info("接收到终端"+clientID+"对平台指令0x"+Integer.toHexString(messageID)+" 流水号 "+serialID+"的应答,处理结果:"+(result==0?"成功":"失败"));

		}
		/*
		LZBusSendData sd=(LZBusSendData)Global.orderAnswer.get(clientID);
		if(sd!=null){
			JTMessageHead head = sd.head;
			if((head.messageID==messageID)&&(head.serialID==serialID))
			{
				sd.setAnswer(true); 
				sd.result=result;
				log.info("接收到终端"+clientID+"对平台指令0x"+Integer.toHexString(messageID)+" 流水号 "+serialID+"的应答,处理结果:"+(result==0?"成功":"失败"));
			}
		}
		*/
	}

	@Override
	/**
	 * 根据消息ID不同处理，其中要考虑4件事
	 * (1)分解消息体
	 * (2)是否是终端对平台指令的应答，并设置应答标识
	 * (3)向平台转发
	 * (4)是否需回复终端
	 * @see cc.gps.active.IProcessReceive#processMessageBody(cc.gps.data.jt808.JTReceiveData)
	 * 
	 */
	
	public byte[] processMessageBody(JTReceiveData data) {  //子类还需重写
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public byte[] processMessageExtral(JTReceiveData data) { //子类还需重写
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	@Override
	final public void saveSessionCache(String clientID, ChannelHandlerContext session,
			long expiredTime) {
		SocketSessionManager.putContent(clientID, session, expiredTime);  //保存终端session供平台下发指令用
		
	}

	@Override
	
	final public void processTrans(JTReceiveData data) {
		Thread thread= new SaveObjTask(data);
		Global.pool.execute(thread);
		//object
		//Task task2=new SaveObjTask(data);
		//Global.tp.addTask(task2);
		/*
		if(Config.getKey("connection").equals("0")) //db
		{
			//object
			Task task2=new SaveObjTask(data);
			Global.tp.addTask(task2);
		}else{
			if(BusinessSessionManager.getCacheMap()!=null){
				//log.debug("向平台转发数据:"+Integer.toHexString(((JTReceiveData)data).head.messageID));
				Global.webData.add((JTReceiveData)data,true);
				
			}else
			{
				//log.debug("平台数据服务没启动或已过期");
				writeTrans2Queue(data);
			}
		}*/
		
	/*
		if(BusinessSessionManager.getCacheMap()!=null){
			//log.debug("向平台转发数据:"+Integer.toHexString(((JTReceiveData)data).head.messageID));
			Global.webData.add((JTReceiveData)data,true);
			
		}else
		{
			//log.debug("平台数据服务没启动或已过期");
			writeTrans2Queue(data);
		}*/
	}

	@Override
	public void processMonitor(List<JTReceiveData> datas) {
		HashMap<String,Cache> map = MonitorSessionManager.getCacheMap();
		//Session mSession=(Session)((MonitorSessionManager.getContent(Config.getKey("monitorID"))).getValue());
		//还需判断是否过期　　待改
		if(map.size()>0){
			for(Iterator<JTReceiveData> it=datas.iterator();it.hasNext();){
				JTReceiveData data = (JTReceiveData)it.next();
				for (String mid:map.keySet()) {
					 Cache cache=(MonitorSessionManager.getContent(mid));
					 ChannelHandlerContext ctx=(ChannelHandlerContext)cache.getValue();
					 try {
						ctx.writeAndFlush(SerializeUtil.serializeObject(data));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}
		}
 	}

	//数据交换平台没启动前,先缓存到队列
	private void writeTrans2Queue(JTReceiveData temp){
		Global.transData.add(temp);
	}
	@Override
	public void write2DB(JTReceiveData data){
		
		Task task = new Save2DBTask(data);
		Global.tp.addTask(task);
		//报警
		Task task1 = new SaveWarnTask(data);
		Global.tp.addTask(task1);
	}
	
	
	@Override
	public List<JTReceiveData> processMessageHead(ByteBuf buffer) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	  //写入日志文件 改为在收到数据后写数据表,该方法不再用
	private void save2Queue(String messageID,String clientID,ByteBuf buffer){
		
		if(buffer==null) return ;
		
		//以下为将报文保存到文本文件
		
		//if(!clientID.equals("013368260872")) return;
		//if(!clientID.equals("013368260861")) return;
		StringBuffer str= new StringBuffer();
		//if(messageID.equals("0071")) return;
		str.append("LZTAXI|");
		//str.append((new Date()).toLocaleString()+" | ");
		str.append((new Date()).toLocaleString()+" | R"+" | ");
		str.append(messageID+" | "+clientID+" | ");
		while(buffer.isReadable()){
			str.append(Ecode.DEC2HEX(buffer.readUnsignedByte(),2));
		}
		str.append("\n");
		Global.logData.add(str.toString());
		//有变化就写日志，性能不高，正式用时删除
		if(log.isDebugEnabled())
			Global.logData.trigger();
		
	}
	
		//-----------------
	
	//@Override
	//处理消息Buffer ,区分出消息头 和消息体部分并保存到ReciveData中 
	public final List<JTReceiveData> processMessageHead(ByteBuf bb,String clientID) {
		List<JTReceiveData> datas = new LinkedList<JTReceiveData>();
		
		//开始处理报文 生成新的报文封装
		JTReceiveData  jtrdata=new JTReceiveData();
		jtrdata.head.start=0x02;
		JTMessageHead head = new JTMessageHead();
		//该长度为 序列号2+messageID 1 +数据长 ,不含结束符03
		int h=bb.getUnsignedByte(1);
		int l=bb.getUnsignedByte(2);
		String lh=Ecode.DEC2HEX(l,2)+Ecode.DEC2HEX(h,2);
		int len=Integer.parseInt(Ecode.HEX2DEC(lh));
		head.len=len;
		//head.len=bb.getUnsignedShort(start+1);//Integer.parseInt(Ecode.HEX2DEC(stemp));  //高位在前
		//序列号
		//head.serialID=bb.getUnsignedShort(start+3);//命令序列号
		//类型
		head.messageID=bb.getUnsignedByte(start+3); //命令ID
		head.phone=clientID;
		//log.info(head.serialID +" "+head.messageID+"  "+head.phone);
		
		//StringBuffer body=new StringBuffer();
		/*
		bb.skipBytes(6);
		while (bb.isReadable()) {
    		String s=Ecode.DEC2HEX(bb.readByte(),2);
			if(s.length()<2)s="0"+s;
			body.append(s);
    	}
		jtrdata.body=body.toString();
		*/
		bb.skipBytes(4);
		jtrdata.bb=bb.duplicate();
		jtrdata.head=head;
		//加入到待转发队列
		datas.add(jtrdata);
		return datas;
	}
}

