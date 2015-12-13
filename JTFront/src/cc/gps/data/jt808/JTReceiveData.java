package cc.gps.data.jt808;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import cc.gps.util.Ecode;

public class JTReceiveData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 20020L;
	//public int start;  //start为99999 为前端服务器回复平台标识

	/*------以下为消息头*-------- */
	public JTMessageHead head=new JTMessageHead();
	public String body=""; //16进制字符串
	public ByteBuf bb;
	//public String clientID;
	 
	public boolean frontAnswerX=false; //用于前端服务器回复平台是否下发终端指令成功
	
	public String toString()
	{
		return "\nhead:"+head+"\nbody:"+body;
	}
	
	public int getCC(){  //根据内容生成校验码，用于比较是否正确
		int cc=0;
		String str=head.toStrings()+body;
		
		cc=Ecode.buildCheckCode(str);
		return cc;
	}
	

}
