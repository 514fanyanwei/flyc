package cc.gps.active.jt808;
/**
 * 交通部报文处理的工厂类
 * 根据报文ID选不同的报文处理器
 * @author FHL
 *
 */

public class JTProcessReceiveBodyFactory {
	public static JTProcessReceive createProcess(int messageID)
	{
		JTProcessReceive jtp=null;
		switch(messageID){
			case 0x0001: jtp=new JTProcess0x0001();	break;
			case 0x0002: jtp=new JTProcess0x0002();	break;
			case 0x0003: jtp=new JTProcess0x0003();	break;
			case 0x0100: jtp=new JTProcess0x0100(); break;
			case 0x0102: jtp=new JTProcess0x0102(); break;
			case 0x0104: jtp=new JTProcess0x0104(); break;
			case 0x0107: jtp=new JTProcess0x0107();	break;
			case 0x0200: jtp=new JTProcess0x0200(); break;
			case 0x0201: jtp=new JTProcess0x0201(); break;
			case 0x0301: jtp=new JTProcess0x0301(); break;
			case 0x0302: jtp=new JTProcess0x0302(); break;
			case 0x0303: jtp=new JTProcess0x0303(); break;
			case 0x0500: jtp=new JTProcess0x0500(); break;
			case 0x0700: jtp=new JTProcess0x0700(); break;
			case 0x0701: jtp=new JTProcess0x0701(); break;
			case 0x0702: jtp=new JTProcess0x0702(); break;
			case 0x0704: jtp=new JTProcess0x0704(); break;
			case 0x0705: jtp=new JTProcess0x0705(); break;
			case 0x0800: jtp=new JTProcess0x0800();break;
			case 0x0801: jtp=new JTProcess0x0801(); break;
			case 0x0802: jtp=new JTProcess0x0802(); break;
			case 0x0900: jtp=new JTProcess0x0900();break;
			case 0x0901: jtp=new JTProcess0x0901();break;
		
		}
		return jtp;
	}
}

