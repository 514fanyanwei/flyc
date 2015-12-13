package cc.gps.data.jt808;

public class JT0x0801 extends JT0x0800{
	/**
	 * 
	 */
	private static final long serialVersionUID = 20018L;
	public JT0x0200  gps;
	public String data="";
	public int replySerialID; //对应的下发指令的流水号
	public int result=0;  //执行成功
	public int messageID;    //与应答的下发指令的消息ID相同
	
}
