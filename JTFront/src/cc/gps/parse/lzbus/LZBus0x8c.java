package cc.gps.parse.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.buffer.ByteBuf;
import cc.gps.active.lzbus.LZBusBuild0x49;
import cc.gps.config.Global;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusLineVersion;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;

public class LZBus0x8c {
	private static final Log log = LogFactory.getLog(LZBus0x8c.class);
	public Segment<String> lineNO; //当前的线路号
	public Segment<String> lineVersion; //当前线路信息版本号
	private int s=0; //开始位置  //测试时改为6
	private String sim;
	public LZBus0x8c(ByteBuf bb,String sim){
		this.sim=sim;
		bb.skipBytes(s);
		lineNO        =new Segment<String>(bb,4,new StringParse());
		lineVersion   =new Segment<String>(bb,7,new StringParse());
	}
	
	public LZBusLineVersion convert(){
		LZBusLineVersion jt = new LZBusLineVersion();
		jt.lineNO=Ecode.BCD2DEC(lineNO.value);
		jt.lineVersion=Ecode.A2S(lineVersion.value);
		/*
		String linenv=Global.LINENV.get(sim);
		if(linenv==null||linenv.equals("")){
			jt.body=lineNO.value+lineVersion.value;
			Global.LINENV.put(sim, lineNO.value+lineVersion.value);
		}else{
			jt.body=linenv;
		}*/
		
		log.info(jt);
		return jt;
	}
}
