package cc.gps.data.jt808;

import cc.gps.util.Ecode;

public class JT0x0200 extends JTReceiveData{
	/**
	 * 
	 */
	private static final long serialVersionUID = 200141L;
	//基本本信息
	public String warning; //  32位二进制? 
	public String status;  // 32位二进制
	
	public long iwarning;
	public long istatus;
	
	public double  latitude; //纬度
	public double longitude; //经度
	public double altitude;//高程
	public double velocity; //速度
	public double orientation;//方向
	public String time;//时间
	
	//附加信息
	public int extID;//附加信息ID   //用于LZBUS时,用作上一站点编号
	public int extLen;//附加信息长度
	
	public double mileage;//对应车上里程表读数
	public double  oilmass ;       //对应车上油量表读数
	public double extSpeed; //行驶记录功能获取的速度
	public long eventID;//需要人工确认报警事件的 ID
	//超速报警附加信息
	public int osPosID; //位置类型   over speed
	public long osAreaID;//区域或路段ID;
	
	//进出区域/路线报警附加信息
	public int oiPosID;//位置类型     1：圆型区域；2：矩形区域；3：多边形区域；4：路线 
	public long oiAreaID; //区域或路段ID;
	public int  oOri;//方向:进或出   0：进； 1：出
	
	//路线行驶时间不足/过长报警附加信息
	public long lineID; //路段ID
	public int  driveTime;//路段行驶时间
	public int driveResult;//结果
	//扩展车辆信号状态位 
	public long signal;
	//IO 状态位
	public long io;
	//模拟量
	public long analog;
	//无线通信网络信号强度
	public int signal_intensity; 
	//GNSS 定位卫星数
	public int satellite_num;
	//后续信息长度
	public long morelen;

	//飘移,false 无   true 漂
	public boolean drift=false;
	
	public String toString(){
		StringBuffer info=new StringBuffer();
		info.append(VStatus.dispNSINFO(istatus));
		/*
		for(int i=0;i<32;i++){
			if((istatus&VStatus.NS[i])!=0)
				info.append(i+"位:"+VStatus.NSINFO[i]+"|");
		}*/
		StringBuffer iwarn=new StringBuffer();
		
		for(int i=0;i<32;i++){
			//log.info(iwarning&VWarns.WS[i]);
			if((iwarning&VWarns.WS[i])!=0)
				iwarn.append(i+"位:"+VWarns.WSINFO[i]+"|");
		}
		
		
		StringBuffer info2=new StringBuffer();
		//System.out.println("^^^^^^^^^^^^^"+signal);
		for(int i=0;i<32;i++){
			if((signal&VSignal.NS[i])!=0)
				info2.append(i+"位:"+VSignal.NSINFO[i]+"|");
		}
		
		StringBuffer info3=new StringBuffer();
		//System.out.println("^^^^^^^^^^^^^"+io);
		for(int i=0;i<16;i++){
			if((io&VIO.NS[i])!=0)
				info3.append(i+"位:"+VIO.NSINFO[i]+"|");
		}
		return "纬度："+latitude+" 经度："+longitude+"\n高程："+altitude+" 速度："+velocity
				+"\n方向："+orientation+" 时间："+time
				+"\n里程:"+mileage+" 油表读数："+oilmass
				+"\n状态"+status+"  "+info.toString()
				+"\n报警"+warning+"  "+iwarn.toString()
				+"\n\n*****位置附加信息**1 基本信息******"
					+"\n车上里程表读数"+mileage
					+"\n车上油量表读数"+oilmass
					+"\n行驶记录功能获取的速度"+extSpeed 
					+"\n需要人工确认报警事件的ID"+eventID
				+"\n\n****位置附加信息****2 超速报警附加信息*****"
					+"\n位置类型                  : "+ osPosID
					+"\n区域或路段ID   :"+osAreaID
				+"\n*****位置附加信息****3 进出区域/路线报警附加信息------"
					+"\n位置类型     1：圆型区域；2：矩形区域；3：多边形区域；4：路线               :"+oiPosID 
					+"\n区域或路段ID                                                 :"+oiAreaID
					+"\n方向:进或出   0：进； 1：出                                                                                                  :"+oOri
				
		+"\n\n****位置附加信息****4路线行驶时间不足/过长报警附加信息-----------------"
			+"\n路段ID                :"+lineID
			+"\n路段行驶时间                           :"+driveTime
			+"\n结果:0：不足；1：过长     : "+driveResult
		 +"\n\n****位置附加信息****5扩展车辆信号状态位-----------------"		
				+"\n车辆信号状态:   "+signal+" \n "+info2.toString()
		+"\n\n****位置附加信息****IO 状态位:  "+io+" \n "+info3.toString()
				
			+"\n模拟量:"+analog
			+"\n无线通信网络信号强度"+signal_intensity 
			+"\nGNSS 定位卫星数"+satellite_num;
			//+"\n后续信息长度"+morelen;
	}
	String dispAnalog(){
		StringBuffer info=new StringBuffer();
		String s=Ecode.hexString2binaryString(Ecode.DEC2HEX(analog,8));
		info.append("AD0:"+Ecode.binToDec(s.substring(0,16)+"  AD1:"+Ecode.binToDec(s.substring(16))));
		return info.toString();
	}
	/*
	private static class Lignt_Status{
		final static long[] LS={
			1                        ,//0    
			2                        ,//1 
			4                        ,//2 
			8                        ,//3 
			16                       ,//4 
			32                       ,//5 
			64                       ,//6 
			128                      ,//7 
			256                      ,//8 
			512                      ,//9 
			1024                     ,//10
			2048                     ,//11
			4096                     ,//12
			8192                     ,//13
			16384                    ,//14
			32768                    ,//15
			65536                    ,//16
			131072                   ,//17
			262144                   ,//18
			524288                   ,//19
			1048576                  ,//20
			2097152                  ,//21
			4194304                  ,//22
			8388608                  ,//23
			16777216                 ,//24
			33554432                 ,//25
			67108864                 ,//26
			134217728                ,//27
			268435456                ,//28
			536870912                ,//29
			1073741824               ,//30
			2147483648l               //31
		};
		final static String[] LSINFO={
			"近光灯信号",                    //0  
			"远光灯信号",                    //1
			"右转向灯信号",                    //2
			"左转向灯信号",                        //3
			"制动信号",            //4
			"倒档信号",        //5
			"雾灯信号",                //6
			"示廓灯",              //7
			"喇叭信号",              //8
			"空调状态",              //9
			"空挡信号",         //10
			"缓速器工作",                  //11
			
			"ABS 工作",       //12 
			"加热器工作",                        //13  
			"离合器状态 ",                        //14  
		};
		String display(long lstatus){
			StringBuffer s=new StringBuffer();
			for(int i=0;i<16;i++){
				if((lstatus&Lignt_Status.LS[i])!=0)
					s.append(i+"位:"+Lignt_Status.LSINFO[i]+"|");
			}
			return s.toString();
		}
	}
	
	private static class IO_Status{
		final static long[] IOS={
			1                        ,//0    
			2                        ,//1 
			4                        ,//2 
			8                        ,//3 
			16                       ,//4 
			32                       ,//5 
			64                       ,//6 
			128                      ,//7 
			256                      ,//8 
			512                      ,//9 
			1024                     ,//10
			2048                     ,//11
			4096                     ,//12
			8192                     ,//13
			16384                    ,//14
			32768                    //15
		};
		final static String[] IOSINFO={
			"深度休眠状态",                    //0  
			"休眠状态"                    //1
		};
		String display(long iostatus){
			StringBuffer s=new StringBuffer();
			for(int i=0;i<16;i++){
				if((iostatus&IO_Status.IOS[i])!=0)
					s.append(i+"位:"+IO_Status.IOSINFO[i]+"|");
			}
			return s.toString();
		}
	}
*/
}
