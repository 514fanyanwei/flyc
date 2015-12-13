package cc.gps.data.jt808;

public class JT0x0201 extends JT0x0200{

	/**
	 * 
	 */
	private static final long serialVersionUID = 20015;
	public int replySerialID; //对应的行驶记录数据采集的流水号
	public int messageID;    //与应答的下发指令的消息ID相同
	public int result=0; //执行成功

}