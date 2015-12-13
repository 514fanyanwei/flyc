package cc.gps.active;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.active.jt808.JTProcessReceive;
import cc.gps.active.lzbus.LZBusProcessReceive;

/**
 * 根据报文开始标识选用不同设备的处理程序
 * @author FHL
 *
 */
public class ProcessReceiveFactory {
	private static final Log log = LogFactory.getLog(ProcessReceiveFactory.class);
	public static IProcessReceive createProcess(int start)
	{
		IProcessReceive jtr=null;
		
		switch(start){
			case 0x7e:jtr=new JTProcessReceive();break;
			//case 0x02:jtr=new LSProcessReceive();break;  //涪陵出租 蓝斯
			case 0x02:jtr=new LZBusProcessReceive();break;  //公交 蓝斯
			default:log.info("收到粘包数据");jtr=new LZBusProcessReceive();break;  //涪陵出租 蓝斯
		}
		//if(start!=0x02) log.info("收到开始标志不是0x02的报文");
		//jtr=new LZBusProcessReceive();  //涪陵出租 蓝斯
		return jtr;
	}
	

}
