package cc.gps.parse.jt808;

import io.netty.buffer.ByteBuf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JT0x0a00;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;

public class PJT0x0A00 {
	private static final Log log = LogFactory.getLog(PJT0x0A00.class);
	
	public Segment<Long> rsa_e; //终端 RSA 公钥{e,n}中的 e
	public Segment<String> rsa_n; //RSA 公钥{e,n}中的 n 

	private int s=0; //开始位置  //测试时改为13
	public PJT0x0A00(ByteBuf bb,int len){
		bb.skipBytes(s);
		rsa_e   =new Segment<Long>(bb,4,new LongParse());
		rsa_n   =new Segment<String>(bb,len-3,new StringParse());
	}
	public JT0x0a00 convert(){
		JT0x0a00 jt=new JT0x0a00();
		jt.rsa_e=rsa_e.value;
		String n=rsa_n.value;
		for(int i=0;i<n.length();i=i+2){
			jt.rsa_n[i/2]=Integer.parseInt(n.substring(i,i+2));
		}
		log.info("终端RSA公钥："+
				" 终端 RSA 公钥{e,n}中的 e         "+rsa_e.value+   
				"\n终端 RSA 公钥{e,n}中的 n          "+rsa_n.value
				);
		return jt;
	}	
}