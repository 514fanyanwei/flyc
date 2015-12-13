package cc.gps.data.jt808;

public class JT0x0104 extends JTReceiveData{
	/**
	 * 
	 */
	private static final long serialVersionUID = 20013L;
	public int replySerialID; //对应的终端参数查询消息的流水号
	public int counts;//应答参数个数
	public String content;
	
	public int messageID;    //与应答的下发指令的消息ID相同
	public int result=0; //执行成功
}