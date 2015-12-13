package cc.gps.active.lzbus;

public class LZBusProcessReceiveBodyFactory {
	public static LZBusProcessReceive createProcess(int messageID)
	{
		LZBusProcessReceive jtp=null;
		switch(messageID){
		case 0x71: jtp=new LZBusProcess0x71();	break;  //终端应答
		case 0x72: jtp=new LZBusProcess0x72();	break;  //车载上传文本信息
		case 0x73: jtp=new LZBusProcess0x73();	break;  //带菜单的调度信息的反馈信息
		case 0x74: jtp=new LZBusProcess0x74();	break;  //车载终端参数
		case 0x75: jtp=new LZBusProcess0x75();	break;  //登陆
		case 0x76: jtp=new LZBusProcess0x76();	break;  //版本信息
		case 0x79: jtp=new LZBusProcess0x79();	break;  //车辆进出站
		case 0x7B: jtp=new LZBusProcess0x7b();	break;  //车机从服务器注销
		case 0x7E: jtp=new LZBusProcess0x7e();	break;  //车辆进出特殊站点
		
		case 0x82: jtp=new LZBusProcess0x82();	break;  //上传特殊区域信息
		case 0x85: jtp=new LZBusProcess0x85();	break;  //线路信息
		case 0x89: jtp=new LZBusProcess0x89();	break;  //司机登录
		case 0x8a: jtp=new LZBusProcess0x8a();	break;  //司机退出
		case 0x8b: jtp=new LZBusProcess0x8b();	break;  //上传司机发车或进入终点的按键操作
		case 0x8c: jtp=new LZBusProcess0x8c();	break;  //查询当前线咱信息版
		case 0x8d: jtp=new LZBusProcess0x8d();	break;  //查询升级服务器信息
		case 0x8f: jtp=new LZBusProcess0x8f();	break;  //终端上送自检信息
		
		
		case 0x41: jtp=new LZBusProcess0x41();	break;  //实时信息
		case 0x42: jtp=new LZBusProcess0x42();	break;  //异常数据
		case 0x43: jtp=new LZBusProcess0x43();	break;  //终端上发车载记录文件
		case 0x44: jtp=new LZBusProcess0x44();	break;  //IC卡数据
		case 0x45: jtp=new LZBusProcess0x45();	break;  //在线保持包
		case 0x46: jtp=new LZBusProcess0x46();	break;  //485中采集数据
		case 0x47: jtp=new LZBusProcess0x47();	break;  //端子5采集数据
		case 0x48: jtp=new LZBusProcess0x48();	break;  //POS采集数据
		
		case 0x49: jtp=new LZBusProcess0x49();	break;  //补传实时信息
		case 0x4a: jtp=new LZBusProcess0x4a(); break;//上传非正常运营请求
		case 0x4b: jtp=new LZBusProcess0x4b(); break;//上传非正常运营请求
		
		case 0x53: jtp=new LZBusProcess0x53();	break;  //超速时长数据
		case 0x54: jtp=new LZBusProcess0x54();	break;  //返回录像模块当前状态
		case 0x55: jtp=new LZBusProcess0x55();	break;  //终端回应中心查询终端存储的限速路段编号
		case 0x56: jtp=new LZBusProcess0x56();	break;  //其他人员IC卡刷卡数据
		
		case 0x90: jtp=new LZBusProcess0x90();	break;  //终端上送自检信息(主动报警)
		case 0x91: jtp=new LZBusProcess0x91();	break;  //带CAN的实时信息
		case 0x92: jtp=new LZBusProcess0x92();	break;  //带CAN的实时信息(需要应答)
		case 0x93: jtp=new LZBusProcess0x93();	break;  //行驶记录数据上传
		
		case 0xa0: jtp=new LZBusProcess0xa0();	break;  //准备上传照片信息
		case 0xa1: jtp=new LZBusProcess0xa1();	break;  //上传照片信息
		case 0xa2: jtp=new LZBusProcess0xa2();break; //车机上传照片完成，请服务器检查
		
			 
			
			
			
			/*
			case 0x43: jtp=new LSProcess0x43();	break; //实时信息
			case 0x46: break;
			case 0x4F: break;
			case 0x41: jtp=new LSProcess0x41();break;  //异常数据
			case 0x6C: jtp=new LSProcess0x6C();break; //脱网信息
			case 0x53: jtp=new LSProcess0x53();break;  //命令执行成功
			case 0x45: break;
			case 0x65: jtp=new LSProcess0x53();break;  //0x65是对参数设置的应答，转为0x53,命令执行成功 
			case 0x56: break;
			case 0x4A: jtp=new LSProcess0x4A();	break; //JPEG图像
			case 0x44: break;
			case 0x72:jtp=new LSProcess0x72();break;  //在线保持包
			case 0x73:break;
			case 0x76:break;
			case 0x77:break;
			case 0x7B:break;
			case 0x7F:jtp =new LSProcess0x7F(); break;//出租车计费
			case 0xFE:break;*/
			
			
		}
		return jtp;
		
	}
}


