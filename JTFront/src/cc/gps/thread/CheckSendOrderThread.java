package cc.gps.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.IProcessReceive;
import cc.gps.active.lzbus.LZBusProcessReceive;
import cc.gps.config.Config;
import cc.gps.config.Global;
import cc.gps.data.SendData;
import cc.gps.data.X2CData;
import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusSendData;
import cc.gps.util.Ecode;

public class CheckSendOrderThread extends Thread{
	private static final Log log = LogFactory.getLog(FetchOrderThread.class);
	private long timeOut=Long.parseLong(Config.getKey("sendTimeOut"));
	
	@Override
	public void run(){
		while(true){
			LinkedList<X2CData> list=null;
			if(!Global.orderSended.isEmpty()){
				list=new LinkedList<X2CData>();
				Set<String> keys=Global.orderSended.keySet();
				for(Iterator<String> it=keys.iterator();it.hasNext();){
					X2CData x2c=Global.orderSended.get(it.next());
					long milisNow = new Date().getTime();
					log.info("已用时间："+(milisNow-x2c.getMilisSendTime())+"  预设超时:"+timeOut);
					if((milisNow-x2c.getMilisSendTime())>timeOut){  //超时，表示终端无应答，不需等待
						//log.info("有发送不成功的数据，发送次数"+(count-1));
						//order=Global.orderAnswer.remove(clientID);    //从待发送队删除，不再发送
						//加入终端应答失败队列
						log.info(x2c.getClientID()+"终端应答失败,发送不成功,停止发送");
						x2c.setDescription("终端无应答，发送不成功,停止发送"); 
						x2c.setResult(1);//1 表示发送失败
						//暂存下发失败指令
						list.add(x2c);
					}
				}
				if(list.size()!=0){
					for(Iterator<X2CData> it=list.iterator();it.hasNext();){
						X2CData data=it.next();
						//告知平台发送失败
						replayFail(data);
						//删除
						Global.orderSended.remove(data.getClientID()+data.getSerialID());
					}
				}
			}
			try {
				sleep(20*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//1分钟检查1次
		}
	}
	
	private void replayFail(X2CData data){
		C2XAnswer td = new C2XAnswer();
		
		//处理发向终端的指令的应答情况
		String clientID=data.getClientID();
		
		//以下三项必须有
		td.head= new JTMessageHead();//)data.head.clone(); //必须有
		td.head.phone=clientID;
		//td.clientID=clientID; //必须有
		
		td.replySerialID=data.getSerialID();
		td.messageID=data.getMessageID();  
		td.head.messageID=0x0001; //转为JT/T-808　的终端通用应答
		td.result=1;  //失败
		
		//转发服务
		if(td.replySerialID<60000){  //小于60000转发  60000以上为系统保留用
			Global.saveObjTaskPool.add(td);
			/*
			Thread thread= new SaveObjTask(td);
			Global.pool.execute(thread);
			*/
			//Task task2=new SaveObjTask(td);
			//Global.tp.addTask(task2);
		}

		//***************************************
		/*
		//存数据库
		LZBusSendData sendData=new LZBusSendData();
		//1 生成消息头 ,供缓存下发指令检查用
		JTMessageHead head = new JTMessageHead();
		head.messageID=data.getMessageID();
		head.serialID=data.getSerialID();
		head.phone = clientID;
		sendData.head=head;
		//2  组装消息头 和 组装消息体
		sendData.body=(String)data.getContent();
		*/
		//saveSendFailOrder(sendData);
	}
	/*
	private static void saveSendFailOrder(SendData obj){
		LZBusSendData data=(LZBusSendData)obj;
		C2XAnswer td = new C2XAnswer();
		
		//处理发向终端的指令的应答情况
		String clientID=data.head.phone;
		

		td.head= new JTMessageHead();//)data.head.clone(); //必须有
		td.head.phone=clientID;
		
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
