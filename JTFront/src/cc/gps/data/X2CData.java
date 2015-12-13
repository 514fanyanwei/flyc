package cc.gps.data;

import java.io.Serializable;
import java.util.Date;

//平台发向前端服务器的数据类型
public class X2CData implements  Serializable,Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1001L;
	int messageID ;   
	/**
 
	 */
	int serialID;
	String pid; //平台ID;
    String clientID;//  终端ID
    int ctid;//终端类型id
	Object content;   //内容  ,不同指令,该字段的类型不同
	int sendCount=1;         //发送次数
	boolean answer=false;    //false表示没有收到应答,true 表示已收到应答，不再发
	int result=0;          //0  处理成功  1 失败
	String description="等待"; 
	int ptype; //0 普通指令  1处理超速
	
	long milisSendTime=0;// = new Date().getTime();
	
	
	
	public X2CData(){}
	public X2CData(int messageID,String clientID,String pid,String content,int ctid){
		this.messageID=messageID;
		this.clientID=clientID;
		this.pid=pid;
		this.content=content;
		this.ctid=ctid;
	}
	public int getMessageID() {
		return messageID;
	}
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	public boolean isAnswer() {
		return answer;
	}
	public void setAnswer(boolean answer) {
		this.answer = answer;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getMilisSendTime() {
		return milisSendTime;
	}
	public void setMilisSendTime(long milisSendTime) {
		this.milisSendTime = milisSendTime;
	}
	public int getSerialID() {
		return serialID;
	}
	public void setSerialID(int serialID) {
		this.serialID = serialID;
	}
	public int getCtid() {
		return ctid;
	}
	public void setCtid(int ctid) {
		this.ctid = ctid;
	}
	public int getPtype() {
		return ptype;
	}
	public void setPtype(int ptype) {
		this.ptype = ptype;
	}
	 public  Object clone()
	 {
		 try 
		 {
			 return  super .clone();
		 }   catch  (CloneNotSupportedException e)
		 {
	         return   null ;
		 } 
	 } 


}
