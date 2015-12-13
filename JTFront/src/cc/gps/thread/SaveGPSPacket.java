package cc.gps.thread;

import io.netty.buffer.ByteBuf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JT0x9999;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;
import cc.gps.util.SerializeUtil;
import cc.util.db.DataSourceUtil;

public class SaveGPSPacket  extends Thread{
	private static final Log log = LogFactory.getLog(SaveGPSPacket.class);
	ByteBuf savebb;
	String ctype;
	String rs;
	String messageID;
	String clientID;
	String body;
	public SaveGPSPacket(ByteBuf savebb,String ctype,String rs,String messageID,String clientID)
	{
		
		this.savebb=savebb;
		
		this.ctype=ctype;
		this.rs=rs;
		this.messageID=messageID;
		this.clientID=clientID;
	}
	
	public void run() {
		//写数据表GPSPacketLog,保存收到的原始报文
    	StringBuffer str= new StringBuffer();
    	
		while(savebb.isReadable()){
			str.append(Ecode.DEC2HEX(savebb.readUnsignedByte(),2));
		}
		body=str.toString();
		
		Packet packet = new Packet();
		packet.ctype=ctype;
		packet.rs=rs;
		packet.messageID=messageID;
		packet.clientID=clientID;
		packet.body=str.toString();
		Global.saveGpsPacketPool.add(packet);//加入线程池执行
		
		
		
	}
}