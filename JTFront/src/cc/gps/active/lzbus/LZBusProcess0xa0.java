package cc.gps.active.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.config.Global;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.parse.lzbus.LZBus0x41;
import cc.gps.parse.lzbus.LZBus0xa0;
import cc.gps.util.Ecode;

public class LZBusProcess0xa0  extends LZBusProcess0x41{
	private static final Log log = LogFactory.getLog(LZBusProcess0xa0.class);
	
	@Override
	
	//该body仅有图像数据相关的准备信息
	public byte[] processMessageBody(JTReceiveData inData) {
		//
		//super.processMessageBody(inData);
		LZBus0xa0 lz=new LZBus0xa0(inData.bb,inData.head.phone);
		LZBusImage lzImage=lz.convert();
		Global.CID2NUM.put(inData.head.phone, lzImage.total);
		Global.CID2BodyArray.put(inData.head.phone, lzImage);
		log.debug("共有"+lzImage.total+"个切片");
		return null;
	}
}