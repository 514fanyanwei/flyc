package cc.net;

import io.netty.channel.ChannelHandlerContext;

public interface ICreatCheckPacket {
	//生成心跳报文
	public  void createHeartBeat(ChannelHandlerContext ctx);
	//生成点名报文
	public  void createRollCall(ChannelHandlerContext ctx);
	//通知下线
	public void down(String clientID);
}
