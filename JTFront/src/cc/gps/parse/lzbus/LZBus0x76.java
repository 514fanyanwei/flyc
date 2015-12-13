package cc.gps.parse.lzbus;

import cc.gps.parse.Segment;

public class LZBus0x76 {
	public Segment<Integer> answerID; //需要应答的那条命令序号
	public Segment<Integer> messageID; //中心下发指令的ID
	public Segment<String> latitude10; //内容
	
}
