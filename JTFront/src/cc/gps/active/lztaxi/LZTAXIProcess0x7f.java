package cc.gps.active.lztaxi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.parse.lztaxi.LZTAXI0x7f;

public class LZTAXIProcess0x7f extends LZTAXIProcessReceive{
	private static final Log log = LogFactory.getLog(LZTAXIProcess0x7f.class);
	
	@Override
	public byte[] processMessageBody(JTReceiveData inData) {
		LZTAXI0x7f lz=new LZTAXI0x7f(inData.bb);
		JTReceiveData lztaxi=lz.convert();
		
		//写数据库
		if(lztaxi!=null){
			log.info("****"+lztaxi);
			//以下项必须
			lztaxi.head=inData.head; //必须有
			write2DB(lztaxi);
		}
		return null;
	}
	
}
