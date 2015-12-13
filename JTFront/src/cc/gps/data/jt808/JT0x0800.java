package cc.gps.data.jt808;

public class JT0x0800 extends JTReceiveData{
	/**
	 * 
	 */
	private static final long serialVersionUID = 20017L;
	public long mid;//多媒体ID
	public int mtype; //多媒体类型
	public int mecode; //多媒体格式编码
	public int eventID; //事件项编号
	public int channelID;  //通道ID
	
	public int messageID;    //与应答的下发指令的消息ID相同
	public int result=0; 
}