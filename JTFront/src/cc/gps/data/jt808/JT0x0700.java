package cc.gps.data.jt808;

public class JT0x0700 extends JTReceiveData{
	/**
	 * 
	 */
	private static final long serialVersionUID = 20016L;
	public int replySerialID; //对应的行驶记录数据采集的流水号
	public int order; //对应平台发出的命令字
	public String content;
	
	public int messageID;    //与应答的下发指令的消息ID相同
	public int result=0; 
}
