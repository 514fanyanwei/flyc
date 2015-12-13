package cc.gps.service;

import io.netty.channel.ChannelHandlerContext;

//与终端连接的Session
public class SocketSession {
	String clientID;
	ChannelHandlerContext ctx;
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public ChannelHandlerContext getSession() {
		return ctx;
	}
	public void setSession(ChannelHandlerContext session) {
		this.ctx = session;
	}
}
