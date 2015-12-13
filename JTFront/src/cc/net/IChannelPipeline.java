package cc.net;

import io.netty.channel.socket.SocketChannel;

public interface IChannelPipeline {
	public void initPipeline(SocketChannel ch) throws Exception;

}
