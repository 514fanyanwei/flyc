package cc.gps.data;

import cc.gps.data.jt808.JTMessageHead;


/*
 * 该类应包括两类数据：
 * 1　下发终端的数据
 * 2  转发接收并分析处理后的数据到另一端口，并须由该端口发向平台　
 */
public class SendData {
	
	public JTMessageHead head;
	String clientID;  //向终端标识为clientID的终端发数据
	byte[] bytes;     //内容
	boolean isAnswer=false;  //默认设为没有应答
	
	int sendCount=0;         //发送次数
	String description="";   //描述
	
	
	
	
	public SendData(){};
	public SendData(String clientID,byte[]bytes){
		this.clientID=clientID;
		this.bytes=bytes;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public boolean isAnswer() {
		return isAnswer;
	}
	public void setAnswer(boolean isAnswer) {
		this.isAnswer = isAnswer;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
