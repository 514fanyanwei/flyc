package cc.net;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class CCSocketServer {
	private static final Log log = LogFactory.getLog(CCSocketServer.class);
	private int port;
	private IChannelPipeline icp;
	public CCSocketServer(int port,final IChannelPipeline icp){
		this.port=port;
		this.icp=icp;
	}
	public void run() throws Exception {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(16);
        EventLoopGroup workerGroup = new NioEventLoopGroup(500);  //1000个工作线程
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.DEBUG))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                	 icp.initPipeline(ch);
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 100)
             .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Start the server.
            ChannelFuture f = bootstrap.bind(port).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
