package cc.gps.active.jt808;
//处理接收数据的父类

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.active.IProcessReceive;
import cc.gps.active.lzbus.LZBusProcessReceive;
import cc.gps.active.lzbus.LZBusProcessReceiveBodyFactory;
import cc.gps.config.Config;
import cc.gps.config.Global;

import cc.gps.data.SendData;
import cc.gps.data.X2CData;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.JTSendData;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.service.BusinessSessionManager;
import cc.gps.service.MonitorSessionManager;
import cc.gps.service.SocketSessionManager;
import cc.gps.service.TransSessionManager;
import cc.gps.thread.Save2DBTask;
import cc.gps.thread.SaveGPSPacket;
import cc.gps.thread.SaveObjTask;
import cc.gps.thread.SaveWarnTask;
import cc.gps.thread.Task;
import cc.gps.thread.ThreadPool;
import cc.gps.util.Cache;
import cc.gps.util.Ecode;
import cc.gps.util.SerializeUtil;

/**
 * 交通部JT/T808报文处理选择程序
 * @author FHL
 *
 */
public  class JTProcessReceive implements IProcessReceive{
	private static final Log log = LogFactory.getLog(JTProcessReceive.class);
	private static int count=0;
	@Override
	/**
	 *  消息处理流程：
	 * 1.Buffer beforeProcess(Buffer buffer) 预处理buffer ,处理Buffer中可能出现的转义符  
	 * 2.List<ReceiveData> processMessageHead(Buffer buffer) 处理消息，区分出消息头 和消息体部分并保存到List<ReciveData>中
	 *   (考虑buffer中可能包含多个数据)
	 * 3.根据messageID选用不同的处理方案  处理消息体
	 *   (1)首次连接（终端鉴权(0x0102)时），保存与终端连接的session(注意　session的生命期),重发发往该终端的命令，并记重发次数
	 *   (2)通过消息体处理器工厂JTProcessBodyFactory寻找相应的消息体处理器,该处理器分析报文后向交换服务器转发
	 *   (3)消息体处理器返回服务器向终端的应答，(若为终端心跳，则刷新session，考虑session的生命期与终端心跳一致或略大)
	 *   (4)同时，向监控平台发送数据,void processMonitor(List<ReceiveData> datas)
	 */
	public final SendData processData(ByteBuf bb,ChannelHandlerContext ctx) {
			
		
		Attribute<String> attr = ctx.channel().attr(Global.CLIENTID);
		//写数据表GPSPacketLog,保存收到的原始报文
		String clientID=attr.get();
		/*
    	StringBuffer str= new StringBuffer();
    	while(savebb.isReadable()){
			str.append(Ecode.DEC2HEX(savebb.readUnsignedByte(),2));
		}*/
		//System.out.println(str.substring(2,6)+"  "+clientID+"  "+str.toString());
		//Global.add("JT808", "R", str.substring(2,6), clientID, str.toString());
		//Task task= new SaveGPSPacket("JT808", "R", str.substring(2,6), clientID, str.toString());
		//System.out.println((count++)+"**************");
		/// 正式使用时关闭
		 ByteBuf savebb=bb.duplicate();
		Thread thread= new SaveGPSPacket(savebb,"JT808", "R", String.valueOf(bb.getUnsignedShort(1)), clientID);
		Global.pool.execute(thread);
		
		
		//Global.pool.execute(task);
		//Global.tp.addTask(task);
    	//savebb.skipBytes(2);
		//Global.add("JT808", "R", String.valueOf(bb.getUnsignedShort(2)), clientID, "",bb);
		/*
		int x=1;
		if(x==1)
		return null;
		*/
		
		//开始处理报文
		//1 处理接收的消息头
		List<JTReceiveData> datas=processMessageHead(bb);//***********
		
		List<Byte> list = new LinkedList<Byte>();
		if((datas==null)||datas.size()==0){
			return null;
		}
		clientID=attr.get();
		if(clientID!=null){
			Global.CID2ONOFF.put(clientID, 1);
		}
		//Global.CID2END.put(clientID,0);//该终端所收报文完整
		//2 根据messageID选用不同的处理方案  处理消息体
		for(Iterator<JTReceiveData> it=datas.iterator();it.hasNext();){
			JTReceiveData jtrdata=(JTReceiveData)it.next();
			if((jtrdata.head.messageID!=0x0200)&&(jtrdata.head.messageID!=0x0102)&&(jtrdata.head.messageID!=0x0002))
			log.info("JTProcessPeceive receive:"+Ecode.DEC2HEX(jtrdata.head.messageID,4));
			//仅在第1次建立连接（终端鉴权(0x0102)时）或检测到终端心跳时，保存与终端连接的session
			//供平台下发数据用
			if((jtrdata.head.messageID==0x0102)||(jtrdata.head.messageID==0x0002)){
				if(jtrdata.head.messageID==0x0102){
					//clientID=Ecode.A2S(jtrdata.body);  //拟设置签权码为clientid  //可更改为其他标识
					clientID=jtrdata.head.phone;         //现将clientID设为sim
					//标记context
					attr.set(clientID);
					
				}
			}
			clientID=attr.get();
			//log.info("收到"+clientID+"发来的报文ID:0x"+Integer.toHexString(jtrdata.head.messageID));
			saveSessionCache(clientID, ctx, Integer.parseInt(Config.getKey("clientSessionExpired")));
			//构造消息体处理器
			JTProcessReceive process=JTProcessReceiveBodyFactory.createProcess(jtrdata.head.messageID);
			
			//内容是从消息流号或消息包封装项后开始(若有)
			/*
			ByteBuf bff=jtrdata.bb.duplicate();
			while(bff.isReadable()){
				log.info(Ecode.DEC2HEX(bff.readUnsignedByte())+"|");
			}log.info("");
			*/
			if(process==null) return null;
			byte[] bs=process.processMessageBody(jtrdata);
			
			if(bs!=null){
				for(int i=0;i<bs.length;i++){
					list.add(bs[i]);
				}
			}
			//写队列，准备写日志文件 2014-5-21关闭，改为存储到数据表GPSPacketLog中
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
		//向监控平台发2014-7-12 close
		//processMonitor(datas);
		
		return sendData;
	}
	
	/**
	 * 处理发往终端clientID的消息为messageID,流水号为serialID的指令的应答情况 
	 */
	//待验证****************
	
	final protected void processAnswer(int messageID,int serialID,int result,String clientID){
		
		log.info("收到终端"+clientID+"对流水号:"+serialID+"的应答");
		log.info("已发送待回应指令数量:"+Global.orderSended.size());
		String key=clientID+serialID;
		/*
		System.out.println(key);
		Set<String> keys=Global.orderSended.keySet();
		for(Iterator<String> it=keys.iterator();it.hasNext();){
			System.out.print(it.next());
		}System.out.println();*/
		if(Global.orderSended.get(key)!=null){
			X2CData data=Global.orderSended.remove(key);
			log.debug(data.getClientID()+" "+data.getContent());
			//log.info("size2  "+Global.orderSended.size());
			log.info("接收到终端"+clientID+"对平台指令0x"+Integer.toHexString(messageID)+" 流水号 "+serialID+"的应答,处理结果:"+(result==0?"成功":"失败"));

		}
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
		Global.saveObjTaskPool.add(data);
		/*
		Thread thread= new SaveObjTask(data);
		Global.pool.execute(thread);*/
		//不经线程启动
		//new SaveObjTask(data).run();
		/*  
		// 自定义线程池
		Task task2=new SaveObjTask(data);
		Global.tp.addTask(task2);
		*/
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
		//位置
		Global.save2DBTaskPool.add(data);
		//报警
		Global.saveWarnTaskPool.add(data);
		
	}
	
	

	
	  //写入日志文件  //用写数据表代换
	private void save2Queue(String messageID,String clientID,ByteBuf buffer){
		
		if(buffer==null) return ;
		//if(!clientID.equals("013368260872")) return;
		//if(!clientID.equals("013368260861")) return;
		StringBuffer str= new StringBuffer();
		//if(messageID.equals("0071")) return;
		str.append("JT808|");
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
	
	
	//处理消息Buffer ,区分出消息头 和消息体部分并保存到ReciveData中 
	@Override
	public List<JTReceiveData> processMessageHead(ByteBuf bb) {
		//bb=beforeProcess(bb);
		List<JTReceiveData> datas = new LinkedList<JTReceiveData>();
		bb.skipBytes(1);//跳过开始标识
		//开始处理报文 生成新的报文封装
		JTReceiveData  jtrdata=new JTReceiveData();
		jtrdata.head.start=0x7e;
		JTMessageHead head = new JTMessageHead();
		//messageID
		head.messageID=bb.readUnsignedShort(); //命令ID
		
		String stemp=Ecode.decToBin(bb.readUnsignedByte())+Ecode.decToBin(bb.readUnsignedByte());
		//log.debug("--消息体属性字段-------"+stemp);  //0000001000001001
		head.len=Ecode.binToDec(stemp.substring(6)); //为从流水号或消息包封装项(若有的话)后到校验前内容的长度
		head.more=Ecode.binToDec(stemp.substring(2,3));
		head.des=Ecode.binToDec(stemp.substring(3,6));
		//phone
		StringBuffer phone=new StringBuffer();
		for(int i=0;i<6;i++)	phone.append(Ecode.decToBCD(bb.readUnsignedByte()));
		head.phone=phone.toString();
		
		//if(!head.phone.equals("015213604733")) return null; //015213604280 015213604849  015213604149 015213603389 渝BD2979
		//if(!head.phone.equals("015213604913")) return null; //015213604280 015213604849  015213604149 015213603389 渝BD2979
		
		//命令序列号    流水号
		head.serialID=bb.readUnsignedShort();
		if(head.more==1){
			head.total=bb.readUnsignedShort(); //分包总包数;
			head.packetID=bb.readUnsignedShort();//包序号
		}
		//log.info("serial:"+Ecode.DEC2HEX(head.serialID,4) +" messageid: "+Ecode.DEC2HEX(head.messageID,4)+" phone: "
		//+head.phone+" len:"+head.len+"  more: "+head.more+"  des:"+head.des+" head.total:"+head.total+" head.packetID:"+head.packetID);
		
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
		//bb.skipBytes(6);
		//bb的内容从消息流号或消息包封装项后开始(若有)
		jtrdata.bb=bb.duplicate();
		jtrdata.head=head;
		//加入到待转发队列
		datas.add(jtrdata);
		
		return datas;
	}
	

	/**
	 * 处理Buffer中可能出现的转义符
	 * 0x7e<---> 0x7d后紧跟0x02
		0x7d<---> 0x7d后紧跟0x01
	 * @param buffer
	 * @return
	 */
	
	private ByteBuf beforeProcess(ByteBuf buffer){
		boolean find=false;
		//buffer.position(0);
		byte[] b=new byte[buffer.capacity()];

		int k=0;
		for(int i=0;i<b.length;i++){
			b[k]=buffer.getByte(i);
			if((buffer.getUnsignedByte(i)==0x7d)&&(buffer.getUnsignedByte(i+1)==0x02)){
				b[k]=(byte) 0x7e;
				find=true;
			}
			if(!find){
				if((buffer.getUnsignedByte(i)==0x7d)&&(buffer.getUnsignedByte(i+1)==0x01)){
					b[k]=(byte) 0x7d;
					find=true;
				}
			}
			if(find){
				i++; //跳过1个;
				find=false;
			}
			k++;
		}
		//byte[] bb = new byte[k];
		//for(int j=0;j<bb.length;j++) bb[j]=b[j];
		ByteBuf bbf=Unpooled.buffer(k);
		//log.info("new:");
		for(int i=0;i<k;i++){
			bbf.writeByte(b[i]);
			//log.info(Ecode.DEC2HEX(b[i],2)+"|");
		}
		//log.info("");
		return bbf;
	}
	
}
