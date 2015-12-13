package cc.gps.active.lzbus;

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;
import cc.gps.data.Recourse;
import cc.gps.data.jt808.JT0x0801;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.parse.lzbus.LZBus0xa1;
import cc.gps.util.Ecode;

public class LZBusProcess0xa1 extends LZBusProcessReceive{
	private static final Log log = LogFactory.getLog(LZBusProcess0xa1.class);
	
	@Override
	//该body仅有图像数据
	public byte[] processMessageBody(JTReceiveData inData) {
		
		LZBus0xa1  lz=new LZBus0xa1(inData.bb,inData.head.phone);
		//LZBusImage img=lz.convert();
		lz.convert();
		return null;
	}
}
