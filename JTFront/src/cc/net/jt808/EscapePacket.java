package cc.net.jt808;

import io.netty.buffer.ByteBuf;

public class EscapePacket {
	public int count; // 转义字符个数
	public ByteBuf bb;//转义后的结果
	public boolean isAll=false;//是否完整包  true:完整

}
