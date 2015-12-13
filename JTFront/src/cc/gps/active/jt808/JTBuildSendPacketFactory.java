package cc.gps.active.jt808;

import cc.gps.active.lzbus.LZBusBuildSendPacket;


public class JTBuildSendPacketFactory {
	public static JTBuildSendPacket createBuild(int messageID)
	{
		JTBuildSendPacket jbs=null;
		switch(messageID){
			case 0x8001: jbs=new JTBuild0x8001(); break; //平台通用应答
			case 0x8100: jbs=new JTBuild0x8100(); break;//终端注册
			//case 0x8800: jbs=new JTBuild0x8800(); break; //多媒体数据上传应答  改为直接生成
			/*
			case 0x8201: jbs=new JTBuild0x8201(); break; //查询终端位置
			//---------
			case 0x8103: jbs=new JTBuild0x8103(); break; //设置终端参数
			case 0x8104: jbs=new JTBuild0x8104(); break; //查询终端参数
			  
			case 0x8202: jbs=new JTBuild0x8202(); break; //监时位置跟踪
			
			case 0x8800: jbs=new JTBuild0x8800(); break; //多媒体数据上传应答
			case 0x8801: jbs=new JTBuild0x8801(); break; //摄像头立即拍摄   
			case 0x8802: jbs=new JTBuild0x8802(); break; //存储多媒体数据检索
			case 0x8803: jbs=new JTBuild0x8803(); break; //存储多媒体数据上传命令
			case 0x8804: jbs=new JTBuild0x8804(); break; //录音开始命令
			case 0x8700: jbs=new JTBuild0x8700(); break; //行驶记录数据采集命令
			
			case 0x8600: jbs= new JTBuild0x8600(); break;  //设置圆形区域
			case 0x8602: jbs= new JTBuild0x8602(); break;  //设置矩形区域
			case 0x8604: jbs= new JTBuild0x8604(); break;  //设置多边形区域
			
			case 0x8601: jbs= new JTBuild0x8601(); break;  //取消圆形区域
			case 0x8603: jbs= new JTBuild0x8603(); break;  //取消矩形区域
			case 0x8605: jbs= new JTBuild0x8605(); break;  //取消多边形区域
			case 0x8606: jbs= new JTBuild0x8606(); break;  //设置路线
			case 0x8607: jbs= new JTBuild0x8607(); break;  //取消路线
			
			case 0x8300: jbs= new JTBuild0x8300(); break; //文本信息下发
			case 0x8302: jbs= new JTBuild0x8302(); break;  //提问下发
			case 0x8303: jbs= new JTBuild0x8303(); break;  //信息点播菜单
			case 0x8304: jbs=new JTBuild0x8304(); break; //信息服务	
			*/
			default: jbs=new JTBuildSendPacket(messageID);break;  //默认用这个
			
		}
		return jbs;
		
	}

}
