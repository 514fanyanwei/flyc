package cc.gps.data.jt808;

//终端对指令的通用应答
public class C2XAnswer extends JTReceiveData{
	/**
	 * 
	 */
	private static final long serialVersionUID = 20010L;

	public int replySerialID;  //与应答的下发指令的流水号相同
	public int messageID;    //与应答的下发指令的消息ID相同
	
	public int result;  //0 执行成功  1执行失败  -1发送失败
	
	public JTReceiveData attachment; //其他应答指令的消息体
	
	public String toString(){
		return head+" \nreplySerialID:"+replySerialID+
				" messageID:"+messageID+
				" result:"+result;
	}

}
