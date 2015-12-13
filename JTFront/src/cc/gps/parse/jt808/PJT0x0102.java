package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.LinkedList;

import cc.gps.data.jt808.JT0x0102;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.jt808.VWarns;
import cc.gps.parse.DateTimeParse;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.parse.lzbus.LZBus0x41;
import cc.gps.util.Ecode;
import cc.gps.util.FileUtil;

public class PJT0x0102 {
	public Segment<String> authCode; //鉴权码
	
	private int s=0; //开始位置  //测试时改为13
	
	
	public PJT0x0102(ByteBuf bb,int n){
		bb.skipBytes(s);
		authCode	   =new Segment<String>(bb,n,new StringParse());
	}
	
	public JT0x0102 convert(){
		JT0x0102 jt=new JT0x0102();
		jt.authorization_code=Ecode.A2S(authCode.value);//Ecode.A2S(inData.body)
		return jt;
	}
	
}