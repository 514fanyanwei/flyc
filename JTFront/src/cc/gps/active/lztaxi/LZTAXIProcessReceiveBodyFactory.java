package cc.gps.active.lztaxi;

public class LZTAXIProcessReceiveBodyFactory {
	public static LZTAXIProcessReceive createProcess(int messageID)
	{
		LZTAXIProcessReceive jtp=null;
		switch(messageID){
			case 0x41: jtp=new LZTAXIProcess0x41();break;  //异常数据
			case 0x4C: jtp=new LZTAXIProcess0x4C();	break;  //登陆
			case 0x43: jtp=new LZTAXIProcess0x43();	break; //实时信息
			case 0x53: jtp=new LZTAXIProcess0x53();break;  //命令执行成功
			case 0x4A: jtp=new LZTAXIProcess0x4a();	break; //JPEG图像
			case 0x72: jtp =new LZTAXIProcess0x72();break;  //在线保持包
			
			case 0x46: jtp=new LZTAXIProcess0x46();break; //到达定点数据
			case 0x4f: jtp=new LZTAXIProcess0x46();break; //离开达定点数据
			case 0x6c: jtp=new LZTAXIProcess0x6c();break; //脱网信息
			case 0x45: jtp=new LZTAXIProcess0x45();break; //命令执行失败
			case 0x65: jtp=new LZTAXIProcess0x65();break; //终端设置应答
			case 0x56: jtp=new LZTAXIProcess0x56();break; //版本信息
			case 0x44: jtp=new LZTAXIProcess0x44();break; //普通数据
			//case 0x7a: jtp=new LZTAXIProcess0x7a();break; //带CAN数据的实时信息
			case 0x76: jtp=new LZTAXIProcess0x76();break; //485口采集数据
			//case 0x77: jtp=new LZTAXIProcess0x77();break; //查询呼叫接听限制应答
			//case 0x7b: jtp=new LZTAXIProcess0x7b();break; //返回录像模块当前状态
			case 0xfe: jtp=new LZTAXIProcess0xfe();break; //串口透明传传输数据
			case 0x7c: jtp=new LZTAXIProcess0x7c();break; //带菜单的调度信息的反馈信息
			case 0x55: jtp=new LZTAXIProcess0x55();break; //IC卡数据
			
			case 0x7e: 
				System.out.println("青岛恒星计价器");
				jtp=new LZTAXIProcess0x7f();break; //青岛恒星计价器*/
			case 0x7f: 
				System.out.println("南京通用计价器");
				jtp=new LZTAXIProcess0x7f();break; //南京通用计价器
			
			//case 0x80: jtp=new LZTAXIProcess0x80();break; //温湿度数据
			case 0x82: 
				System.out.println("杭州盈晖计价器");
				jtp=new LZTAXIProcess0x7f();break; //杭州盈晖计价器*/
			/*
			case 0x4F: break;
			
			case 0x6C: jtp=new LSProcess0x6C();break; //脱网信息
			
			case 0x45: break;
			case 0x65: jtp=new LSProcess0x53();break;  //0x65是对参数设置的应答，转为0x53,命令执行成功 
			case 0x56: break;
			
			case 0x44: break;
			case 0x72:jtp=new LSProcess0x72();break;  //在线保持包
			case 0x73:break;
			case 0x76:break;
			case 0x77:break;
			case 0x7B:break;
			case 0x7F:jtp =new LSProcess0x7F(); break;//出租车计费
			case 0xFE:break;
			
			*/
		}
		return jtp;
		
	}
}
