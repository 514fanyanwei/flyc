package cc.gps.data.jt808;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.util.Ecode;

public class JTMessageHead implements  Serializable,Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 20019L;

	/**
	 * 
	 */
	

	private static final Log log = LogFactory.getLog(JTMessageHead.class);
	public int start;//报文开始标识
	public int messageID; //消息ID;
	//消息体属性
	public int len;  //消息体长度
	public int des;  //是否加密
	public int more; //是否有分包

	public String phone="013812345678"; //消息头默认手机号

	public int serialID;	//流水号 
		//消息包封装项
	public int total=-1;    //消息包总数
	public int packetID=-1; //包序号
	
	public JTMessageHead(int messageID,int len,int des,int more,String phone,int serialID,int total,int packetID){
		this.messageID=messageID;
		this.len=len;
		this.des=des;
		this.phone=phone;
		this.serialID=serialID;
		this.total=total;
		this.packetID=packetID;
		
	}
	public JTMessageHead(){}
	
	public String toString()
	{
		return "messageID:"+messageID+" len:"+len+" des:"+des+" more:"+more+" phone:"+phone+" serialID:"+serialID+" total:"+total +" packetID:"+packetID;
	}
	//生成发送消息头16进制字符串
	public String toStrings(){
		StringBuffer str = new StringBuffer();
		//str.append("7e");
		//byte[] bs=new byte[len+more==0?12:16];
		String temp=Ecode.DEC2HEX(messageID);
		while(temp.length()<4) temp="0"+temp;
		str.append(temp);
		//组装消息体属性
		if(more==0) temp="000";  //2位保留+1位标识
		else temp="001";
		
		if(des==0) temp+="000";
		else temp+="001";
		String temp2=Ecode.decToBin(len);
		//log.info("temp2:::::::::"+temp2);
		while(temp2.length()<10) temp2="0"+temp2;
		//log.info("temp2 after:::::::::"+temp2);
		temp+=temp2;
		//log.info("temp:::::::::"+temp);
		str.append(Ecode.binaryString2hexString(temp));
		//log.info("--"+Ecode.binaryString2hexString(temp));
		temp="";
		//组装终端手机号
		for(int i=0;i<phone.length();i++){
			temp+=Ecode.decTo4BCD(Integer.parseInt(phone.substring(i,i+1)));
		}
		str.append(temp);
		//组装流水号
		temp=Ecode.DEC2HEX(serialID);
		while(temp.length()<4) temp="0"+temp;
		str.append(temp);
		if(more!=0){
			//组装  消息包封装项
			temp=Ecode.HEX2DEC(String.valueOf(total));
			while(temp.length()<4) temp="0"+temp;
			str.append(temp);
			
			temp=Ecode.HEX2DEC(String.valueOf(packetID));
			while(temp.length()<4) temp="0"+temp;
			str.append(temp);
		}
			
		//log.debug("JTMessageHead ,send:"+str);
		return str.toString();
	}
	
	
	 	public Object clone(){
	 		JTMessageHead o = null;
	        try{
	            o = (JTMessageHead)super.clone();
	        }catch(CloneNotSupportedException e){
	            e.printStackTrace();
	        }
	        return o;
	    }
	
}
