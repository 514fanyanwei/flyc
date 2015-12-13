package cc.gps.data.jt808;
/*
public enum VStatus {
	STOP,ONLine,OFFLine,SWARN,WARN,FULL,EMPTY;
}*/
public class VStatus{
	  public final static long[] NS={
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
	public final static String[] NSINFO={
		 "ACC开",             //0         0关         1开  
		 "定位",              //1         0未定位
		 "南纬",              //2         0北纬    1 南纬 
		 "西经",              //3         0东经
		 "停运状态",          //4         0停运
		 "经纬度加密",        //5         0未加密
		 "",                  //6      
		 "补传数据",                  //7      (自定义)
		 "",                  //8
		 "",                  //9      
		 //"满载",              //9  //8,9联合判断  00空车 01半载   10 保留  11满载 //纳入 dispNSINFO()方法中分析      
		 "车辆油路断开",      //10   0正常           
		 "车辆电路断开",      //11   0正常          
		 "车门加锁",          //12   0解锁
		"前门开",//13位
		"中门开",//14位
		"后门开",//15位
		"驾驶室门开",//16位
		"自定义门开",//17位
		 /*
		 "营运中",             //13  扩展位  //LZBus  11位
		 "上行" ,            //14  扩展位  //LZBus  12位
		
		 "包车申请",                        //15  LZBUS
		 "加油申请",                        //16  LZBUS
		 "故障报修",                     //17   LZBUS*/
		 "使用 GPS 卫星进行定位",//18   0未使用
		 "使用北斗卫星进行定位",//19      0未使用
		 "使用 GLONASS 卫星进行定位",//20  0未使用
		 "使用 Galileo 卫星进行定位",//21  0未使用
		 
	};
	
	public static String dispNSINFO(long istatus){
		StringBuffer info=new StringBuffer();
		for(int i=0;i<32;i++){
			if((i==8)||(i==9)) continue;
			if((istatus&VStatus.NS[i])!=0)
				info.append(i+"位:"+VStatus.NSINFO[i]+"|");
		}
		//System.out.println((istatus&VStatus.NS[8])+" ####  "+(istatus&VStatus.NS[9]));
		if(((istatus&VStatus.NS[8])==0)&&((istatus&VStatus.NS[9])==0))
			info.append("8,9位:空载");
		if(((istatus&VStatus.NS[8])==0)&&((istatus&VStatus.NS[9])!=0))
			info.append("8,9位:半载");
		if(((istatus&VStatus.NS[8])!=0)&&((istatus&VStatus.NS[9])==0))
			info.append("8,9位:保留");
		if(((istatus&VStatus.NS[8])!=0)&&((istatus&VStatus.NS[9])!=0))
			info.append("8,9位:满载");
		return info.toString();
	}
}

