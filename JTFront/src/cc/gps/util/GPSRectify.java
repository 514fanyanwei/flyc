package cc.gps.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.lzbus.LZBusProcess0x41;
import cc.gps.config.Global;
import cc.gps.data.jt808.JT0x0200;

/**
* gps坐标纠偏算法，适用于百度、google地图, 勿外传！
* @copyright 重庆城卡电子技术有限公司
* @author fanhl
* @date 2013-8-21
*/
public final class GPSRectify {
	double casm_rr;     
	long casm_t1; 
	long casm_t2;      
	double casm_x1;         
	double casm_y1;         
	double casm_x2;      
	double casm_y2;    
	double casm_f;        
	final static double pi = 3.14159265358979324;
	private double x_pi =    pi * 3000.0 / 180.0;
	
	final static double a = 6378245.0;
	final static double ee = 0.00669342162296594323;
	private static final Log log = LogFactory.getLog(GPSRectify.class);
	
	static double lat[]={29.705858,
		29.705795,
		29.705748,
		29.705716,
		29.705932,
		29.70616,
		29.706458,
		29.706858,
		29.707234,
		29.70761,
		29.707991,
		29.708422,
		29.708681,
		29.708959,
		29.709187,
		29.70914,
		29.709132,
		29.709112,
		29.708536,
		29.708014,
		29.707665,
		29.707081,
		29.706748,
		29.706458,
		29.705936,
		29.705528,
		29.705379,
		29.705269,
		29.705223

};
	static double lon[]={105.716541,
		105.716769,
		105.716994,
		105.717127,
		105.717127,
		105.717137,
		105.717151,
		105.717151,
		105.717165,
		105.717183,
		105.717206,
		105.717184,
		105.717184,
		105.71718 ,
		105.717189,
		105.71765 ,
		105.718422,
		105.718933,
		105.718927,
		105.7189  ,
		105.718886,
		105.718881,
		105.718873,
		105.718863,
		105.718863,
		105.718839,
		105.718844,
		105.719233,
		105.719413 
};
	
	
    static GPSRectify gps=null;
    public static GPSRectify getInstance(){
    	if(gps==null){
    		gps = new GPSRectify();
    	}
    	return gps;
    }
    private GPSRectify(){   }
    
    //GPS 转google坐标（即 GPS 加偏）
  	public Point encryptGoogle(double wgLon,double wgLat) {
  		if (outOfChina(wgLat, wgLon)) {
  			return null;
  		}
  		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
  		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
  		double radLat = wgLat / 180.0 * pi;
  		double magic = Math.sin(radLat);
  		magic = 1 - ee * magic * magic;
  		double sqrtMagic = Math.sqrt(magic);
  		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
  		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
  		Point p = new Point();
  		p.setLatitude(wgLat + dLat);
  		p.setLongitude(wgLon + dLon);
  		return p;
  	}
    //google坐标 转gps（即 GPS 纠偏）
  	public Point decryptGoogle(double google_lon,double google_lat){
		Point p= new Point();
		p=gps.china2wg(google_lon,google_lat);
		return p;
	}
    
	//gps转换成 百度坐标（即 GPS 加偏）
	public Point encryptBaidu(double lon,double lat)  
	{
		Point p=encryPoint(lon,lat);
	    double x = p.getLongitude(), y = p.getLatitude();  
	    double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);  
	    double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);  
	    p.setLongitude(z * Math.cos(theta) + 0.0065);  
	    p.setLatitude(z * Math.sin(theta) + 0.006);
	    return p;
	}  
	//百度 坐标转换成gps 坐标（即 GPS 纠偏）
	public Point decryptBaidu(double bd_lon,double bd_lat)  
	{  
	    double x = bd_lon - 0.0065, y = bd_lat - 0.006;  
	    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);  
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);  
	    Point p = new Point();
	    p.setLongitude(z * Math.cos(theta));  
	    p.setLatitude(z * Math.sin(theta));
	    //p=decryPoint(p.getLongitude(),p.getLatitude());
	    p=china2wg(p.getLongitude(),p.getLatitude());
	    return p;
	}  
	//计算车辆球面最短距离
	public static double sphericalDistance(JT0x0200 p1,JT0x0200 p2){
		/*
		 d(x1,y1,x2,y2)=r*arccos(sin(x1)*sin(x2)+cos(x1)*cos(x2)*cos(y1-y2))
			x1,y1是纬度\经度的弧度单位，r为地球半径
		 */
		
		double x1=1.0*p1.latitude/180*Math.PI;
		double x2=1.0*p2.latitude/180*Math.PI;
		double y1=1.0*p1.longitude/180*Math.PI;
		double y2=1.0*p2.longitude/180*Math.PI;
		double spherical_distance=Math.acos(Math.sin(x1)*Math.sin(x2)
				+Math.cos(x1)*Math.cos(x2)*Math.cos(y1-y2))*6378137;
		return spherical_distance;
	}
	
	//GPS漂移解决
	public static JT0x0200 driftAdjust(JT0x0200 p1,JT0x0200 p2,double time){
		/*
		 d(x1,y1,x2,y2)=r*arccos(sin(x1)*sin(x2)+cos(x1)*cos(x2)*cos(y1-y2))
			x1,y1是纬度\经度的弧度单位，r为地球半径
		 */
		boolean drift=false;
		double x1=1.0*p1.latitude/180*Math.PI;
		double x2=1.0*p2.latitude/180*Math.PI;
		double y1=1.0*p1.longitude/180*Math.PI;
		double y2=1.0*p2.longitude/180*Math.PI;
		/*
		double spherical_distance=Math.acos(Math.sin(x1)*Math.sin(x2)
				+Math.cos(x1)*Math.cos(x2)*Math.cos(y1-y2))*6378137;
		*/
		double spherical_distance=sphericalDistance(p1,p2);
		//double time=Math.abs(DateUtil.DateDiff(p1.time,p2.time,3));
		double v1=p1.velocity;
		double v2=p2.velocity;
		
		if((Math.abs(v1-0)<1)&&(Math.abs(v2-0)<1)){ //车辆没有移动
			//log.info("速度零 漂移");
			p2.drift=true;
			
			p2.latitude=p1.latitude;
			p2.longitude=p1.longitude;
			p2.orientation=p1.orientation;
			
			drift=true;
		}
		
		
		/*
		if(v1>v2){
			double temp=v1;
			v1=v2;
			v2=temp;
		}
		double distance=Math.abs((0.1*v1+0.8*v2)*1000.0/3600)*time;
		
		
		if((Global.CID2Miles.get(p1.head.phone)!=null)&&(Global.CID2Miles.get(p1.head.phone)!=0)){
			distance+=Global.CID2Miles.get(p1.head.phone);
		}
		//漂移阈值 
		//double threshold= time*120*1000.0/3600;
		//log.debug("GPSRectify:球面距离"+spherical_distance+"::------------行驶距离:"+distance+" 上次速度  "+p1.velocity+"  本次速度  "+p2.velocity+" 时离  "+time+" 漂移阈值 "+time*80*1000.0/3600);
		log.debug("GPSRectify:球面距离"+spherical_distance+"::------------行驶距离:"+distance+" 时间间隔  "+time+" 漂移阈值 "+time*80*1000.0/3600+" **   "+p2.time+"  p1.v"+p1.velocity+"  p2.v:"+p2.velocity);
		//球面距离大于一定距离才考虑是否飘移
		
		//以下情况无漂移
		// 1. spherical_distance<10&&distance>0
		
		// distance>0的情况下
		// 2. spherical_distance<100&&distance>0&&spherical_distance/distance<=3
		
		
		if(((distance==0)&&((x1!=x2)||(y1!=y2)))||((x2==0)&&(y2==0))
				||((spherical_distance<=100)&&(spherical_distance/distance)>3)
				||((spherical_distance>100)&&(spherical_distance/distance)>2)
				){  //漂移   计算两点间最短距离/实际行驶距离 >1.5
			log.info("出现漂移");
			//int num=Global.CID2DriftCount.get(p1.head.phone);
			
			p2.drift=true;
			
			//抛弃该点的经纬度，用上一点的值代替
			p2.latitude=p1.latitude;
			p2.longitude=p1.longitude;
			////抛弃当前点后,累计里程会发生变化,以后重新取回

			Global.CID2Miles.put(p1.head.phone, distance);//保存上一位置的累计里程
			drift=true;
			
		}else{
			log.debug("无漂移,--------清零飘移次数");
			//Global.CID2Inertial.put(p1.head.phone, 1);
			Global.CID2Miles.put(p1.head.phone, 0.0);//清零
			//Global.CID2DriftCount.put(p1.head.phone, 0); //清零连续飘移次数
		}*/
		return p2;
	}
    private double yj_sin2(double x) {    
		double tt;             
		double ss;               
		int ff;         
		double s2;  
		int cc;            
		ff = 0;             
		if (x < 0) {         
			x = -x;             
			ff = 1;           
			}           
		cc = (int) (x / 6.28318530717959);    
		tt = x - cc * 6.28318530717959;        
		if (tt > 3.14159265358979324) {          
			tt = tt - 3.14159265358979324;
			
			if (ff == 1)                  
				ff = 0;                      
			else if (ff == 0)                  
				ff = 1;              
			}              
		x = tt;             
		ss = x;          
		s2 = x;               
		tt = tt * tt;               
		s2 = s2 * tt;            
		ss = ss - s2 * 0.166666666666667;       
		s2 = s2 * tt;           
		ss = ss + s2 * 8.33333333333333E-03;         
		s2 = s2 * tt;                
		ss = ss - s2 * 1.98412698412698E-04;        
		s2 = s2 * tt;               
		ss = ss + s2 * 2.75573192239859E-06;       
		s2 = s2 * tt;              
		ss = ss - s2 * 2.50521083854417E-08;      
		if (ff == 1)                       
			ss = -ss;                  
		return ss;     
	}          
	private double Transform_yj5(double x, double y) {       
		double tt;                
		tt = 300 + 1 * x + 2 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.sqrt(x * x));    
		tt = tt + (20 * yj_sin2(18.849555921538764 * x) + 20 * yj_sin2(6.283185307179588 * x)) * 0.6667; 
		tt = tt + (20 * yj_sin2(3.14159265358979324 * x) + 40 * yj_sin2(1.047197551196598 * x)) * 0.6667; 
		tt = tt + (150 * yj_sin2(0.2617993877991495 * x) + 300 * yj_sin2(0.1047197551196598 * x)) * 0.6667;  
		return tt;      
		}       
	private double Transform_yjy5(double x, double y) {   
			double tt;       
			tt = -100 + 2 * x + 3 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.sqrt(x * x));   
			tt = tt + (20 * yj_sin2(18.849555921538764 * x) + 20 * yj_sin2(6.283185307179588 * x)) * 0.6667;  
			tt = tt + (20 * yj_sin2(3.14159265358979324 * y) + 40 * yj_sin2(1.047197551196598 * y)) * 0.6667; 
			tt = tt + (160 * yj_sin2(0.2617993877991495 * y) + 320 * yj_sin2(0.1047197551196598 * y)) * 0.6667; 
			return tt;        
		}      
	private double Transform_jy5(double x, double xx) {       
		double n;               
		double a;                 
		double e;                 
		a = 6378245;                 
		e = 0.00669342;                 
		n = Math.sqrt(1 - e * yj_sin2(x * 0.0174532925199433) * yj_sin2(x * 0.0174532925199433));   
		n = (xx * 180) / (a / n * Math.cos(x * 0.0174532925199433) * 3.14159265358979324);   
		return n;         
	}        
	private double Transform_jyj5(double x, double yy) {   
		double m;                 
		double a;           
		double e;                
		double mm;                 
		a = 6378245;               
		e = 0.00669342;                 
		mm = 1 - e * yj_sin2(x * 0.0174532925199433) * yj_sin2(x * 0.0174532925199433);  
		m = (a * (1 - e)) / (mm * Math.sqrt(mm));                
		return (yy * 180) / (m * 3.14159265358979324);       
	}       
	      
	private double random_yj() {   
		int t;                
		int casm_a;               
		int casm_c;                 
		casm_a = 314159269;               
		casm_c = 453806245;                 
		casm_rr = casm_a * casm_rr + casm_c;    
		t = (int) (casm_rr / 2);              
		casm_rr = casm_rr - t * 2;           
		casm_rr = casm_rr / 2;       
		return (casm_rr);        
		}          
	private void IniCasm(long w_time, long w_lng, long w_lat) {          
		int tt;          
		casm_t1 = w_time;       
		casm_t2 = w_time;               
		tt = (int) (w_time / 0.357);              
		casm_rr = w_time - tt * 0.357;                
		if (w_time == 0)                       
			casm_rr = 0.3;         
		casm_x1 = w_lng;               
		casm_y1 = w_lat;           
		casm_x2 = w_lng;            
		casm_y2 = w_lat;             
		casm_f = 3;     
		}       
	
	private Point encryPoint(double x, double y) {    //x lon  y lat
		 double x1, tempx;         
		 double y1, tempy;               
		 x1 = x * 3686400.0;                
		 y1 = y * 3686400.0;                 
		 int gpsHeight = 0;                
		 int gpsWeek = 0;               
		 int gpsWeekTime = 0;                 
		 double newx, newy;                  

		 Point point = wg2china(1, (int) x1, (int) y1, gpsHeight, gpsWeek, gpsWeekTime);   
		 newx = point.getLatitude();       
		 newy = point.getLongitude();         
		 tempx = newx;                  tempy = newy;      
		 tempx = tempx / 3686400.0;          
		 tempy = tempy / 3686400.0;            
		 point = new Point();               
		 point.setLatitude(tempx);            
		 point.setLongitude(tempy);           
		 return point;       
	}
	
	private static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	private static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
		return ret;
	}
		
		
	private Point wg2china(int wg_flag, long wg_lng, long wg_lat, int wg_heit, int wg_week, long wg_time) { 
		double x_add;               
		double y_add;             
		double h_add;                
		double x_l;                
		double y_l;                
		double casm_v;                
		double t1_t2;                
		double x1_x2;                
		double y1_y2;               
		Point point = null;          
		if (wg_heit > 5000) {              
			return point;           
			}     
		x_l = wg_lng;     
		x_l = x_l / 3686400.0; 
		y_l = wg_lat;                
		y_l = y_l / 3686400.0;             
		if (x_l < 72.004) {              
			return point;              
			}                
		if (x_l > 137.8347) {      
			return point;              
			}             
		if (y_l < 0.8293) {      
			return point;         
			}                
		if (y_l > 55.8271) {    
			return point;       
			}             
		if (wg_flag == 0) {      
			IniCasm(wg_time, wg_lng, wg_lat);
			point = new Point();         
			point.setLatitude(wg_lng);    
			point.setLongitude(wg_lat);       
			return point;              
			}                  
		casm_t2 = wg_time;          
		t1_t2 = (double) (casm_t2 - casm_t1) / 1000.0;     
		if (t1_t2 <= 0) {                   
			casm_t1 = casm_t2;                     
			casm_f = casm_f + 1;                        
			casm_x1 = casm_x2;                        
			casm_f = casm_f + 1;                         
			casm_y1 = casm_y2;               
			casm_f = casm_f + 1;               
			} else {               
				if (t1_t2 > 120) {   
					if (casm_f == 3) {     
						casm_f = 0;         
						casm_x2 = wg_lng;      
						casm_y2 = wg_lat;      
						x1_x2 = casm_x2 - casm_x1;       
						y1_y2 = casm_y2 - casm_y1;       
						casm_v = Math.sqrt(x1_x2 * x1_x2 + y1_y2 * y1_y2) / t1_t2;  
						if (casm_v > 3185) {                                  
							return (point);                              
							}                       
						}                       
					casm_t1 = casm_t2;          
					casm_f = casm_f + 1;        
					casm_x1 = casm_x2;          
					casm_f = casm_f + 1;        
					casm_y1 = casm_y2;          
					casm_f = casm_f + 1;        
					}                  }        
		x_add = Transform_yj5(x_l - 105, y_l - 35);       
		y_add = Transform_yjy5(x_l - 105, y_l - 35);     
		h_add = wg_heit;           
		x_add = x_add + h_add * 0.001 + yj_sin2(wg_time * 0.0174532925199433) + random_yj();  
		y_add = y_add + h_add * 0.001 + yj_sin2(wg_time * 0.0174532925199433) + random_yj();     
		
		point = new Point();             
		point.setLongitude((long) ((x_l + Transform_jy5(y_l, x_add)) * 3686400));   
		point.setLatitude((long) ((y_l + Transform_jyj5(y_l, y_add)) * 3686400));      
		return (point);      
		}  
	
	
    private Point china2wg(double lon,double lat){
    	
    	Point p =new Point();
    	p= encryPoint(lon, lat);  
    	double d_lat = p.getLatitude() - lat;  
    	double d_lon = p.getLongitude() - lon ;
    	p.setLatitude(lat - d_lat);
    	p.setLongitude(lon - d_lon);
    	return p;
    }
    

    //蓝斯纠偏
    /*
    public static boolean verify(JT0x0200 p){
    	boolean f=false;
    	double min=500;
		int k=-1;
    	//for(int i=0;i<lat.length;i++)
    	{
    		JT0x0200 p0=new JT0x0200();
    		p0.latitude=lat[0];
    		p0.longitude=lon[0];
    		//log.debug("****************************************************************************");
    		//log.debug(sphericalDistance(p,p0));
    		if(min>sphericalDistance(p,p0)){
    			min=sphericalDistance(p,p0);
    			log.debug("开始纠偏:********"+min+"  "+sphericalDistance(p,p0));
    			k=0;
    		}
    	}
    	if(k!=-1){
    		p.latitude=lat[k];
    		p.longitude=lon[k];
    		Global.CID2Inertial.put(p.head.phone, k+1);
    		f=true;
    	}
    	return f;
    }
    
    //转蓝斯格式用
    private static long[] codeXY(double x){
		long [] y=new long[4];
		y[0]= (int)x;
		y[1]=(int)((x-y[0])*60);
		y[2]=(long)(((x-y[0])*60-y[1])*Math.pow(10, 4));
		String s=String.valueOf(y[2]);
		
		
		y[2]=Integer.parseInt(s.substring(0, 2));
		y[3]=Integer.parseInt(s.substring(2, 4));
		
		//log.info(x+"    "+y[0]+" * "+y[1]+" * "+y[2]+"  "+y[3]);
		return y;
	}
    private static String lzxy(double lonlat){
    	String s="";
		long lon0[]=codeXY(lonlat);
		
		//经度
		s+=Ecode.DEC2HEX(lon0[0],2); //度
		s+=Ecode.DEC2HEX(lon0[1],2); //分
		s+=Ecode.DEC2HEX(lon0[2],2); //分的小数
		s+=Ecode.DEC2HEX(lon0[3],2); //分的小数
		//log.info("s="+s);
		return s;
    }
    */
	public static void main(String[] args) {       
		//double lon=106.511378333333;
		//double lat=29.6144916666667;
		//double lon=113.540124;
		//double lat=23.517846;
		//double lon=116.391336;
		//double lat=39.907284; 
		//double lon=106.50783503020504;
		//double lat=29.61505973701866;
		//double lat=29.603632; //106.511799,29.614123 经度：
		//double lon=106.502999;
		GPSRectify gps = GPSRectify.getInstance();
		double lat[]={ 29.703132,
				29.703151,
				29.703095,
				29.703067,
				29.703505,
				29.703906,
				29.704474};
		double lon[]={105.720309,				105.720127,
				105.720438,
				105.720738,
				105.720727,
				105.720727,
				105.72077};
		for(int i=0;i<lat.length;i++){
			Point ret=gps.encryptGoogle(lon[i],lat[i]);  	
			ret=gps.decryptGoogle(ret.getLongitude(), ret.getLatitude());
			log.info("谷歌转回GPS坐标为,  lon:"+ret.getLongitude()+",lat:"+ret.getLatitude());
			//log.info("谷歌转回GPS坐标为(lz),  lon:"+lzxy(ret.getLongitude())+",lat:"+lzxy(ret.getLatitude()));
		}
		/*
		log.info("终端获取的GPS坐标为,lon:"+lon+",lat:"+lat+"\n");
		
		GPSRectify gps = GPSRectify.getInstance();
		log.info("百度坐标与GPS坐标的相互转换:");
		 //GPS坐标转百度坐标（即 GPS 加偏）
		Point ret=gps.encryptBaidu(lon,lat);  	
		 //公司GPS坐标 106.511378333333,29.6144916666667
		log.info("GPS转为百度的坐标为,   lon:"+ret.getLongitude()+",lat:"+ret.getLatitude());   
	     
		//百度到到GPS（即 GPS 纠偏）
		ret=gps.decryptBaidu(ret.getLongitude(),ret.getLatitude());
		
		log.info("百度转回GPS坐标为,  lon:"+ret.getLongitude()+",lat:"+ret.getLatitude());
		 
		log.info();
		
		log.info("谷歌坐标与GPS坐标的相互转换:");
		//GPS坐标转GOOGLE坐标(即GPS加偏)
		ret=gps.encryptGoogle(lon,lat);  	
		log.info("GPS转为谷歌的坐标为,   lon:"+ret.getLongitude()+",lat:"+ret.getLatitude());
		//google转到GPS坐标（即 GPS 纠偏）
		ret=gps.decryptGoogle(ret.getLongitude(), ret.getLatitude());
		log.info("谷歌转回GPS坐标为,  lon:"+ret.getLongitude()+",lat:"+ret.getLatitude());
*/
		/*
LZBUS|2013-11-14 13:27:53	| 020027022741414e1d245525 4c017d01 03342f000c59131114132720010e00000000000b300003
LZBUS|2013-11-14 13:28:09	| 020027022841414e1d24544a 456a1e46 50018600031b22000c5a131114132735010e00000000000b310003
414e1d24

5525456a
544a456a
1d245525 456a1e46   4c017d0103342f000c59131114132720010e00000000000b300003

1d24544a 456a1e46   50018600031b22000c5a131114132735010e00000000000b310003

1d245525   1d24544a
4c017d01  456a1e46
纬度：29.614228 经度：106.511793
纬度：29.614123 经度：106.511799
str[1]="020027022741414e1d245525456a1e464c017d0103342f000c59131114132720010e00000000000b300003";//41
str[2]="020027022841414e1d24544a456a1e4650018600031b22000c5a131114132735010e00000000000b310003";//41
		 *
		 */
		
		//GPS drift test
		/*
		JT0x0200 p1=new JT0x0200();
		JT0x0200 p2=new JT0x0200();
		p1.latitude=29.603632; //106.511799,29.614123 经度：
		p1.longitude=106.502999;
		p2.latitude=29.603596;
		p2.longitude=106.503;
		p1.time="2013-11-13 15:21:36";
		p2.time="2013-11-13 15:21:52";
		GPSRectify.driftAdjust(p1,p2,DateUtil.DateDiff(p2.time,p1.time,3));
		*/
	 } 
	
}

