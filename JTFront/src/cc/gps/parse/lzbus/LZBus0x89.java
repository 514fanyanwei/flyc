package cc.gps.parse.lzbus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.jt808.VWarns;
import cc.gps.data.lzbus.LZBusDriver;
import cc.gps.parse.DateTimeParse;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.util.Ecode;
import cc.gps.util.FileUtil;

public class LZBus0x89 {
	private static final Log log = LogFactory.getLog(LZBus0x89.class);
	public Segment<Integer> validate; //GPS有效标示
	public Segment<Integer> nos; //南北纬度标示
	public Segment<Integer> latitude10; //纬度 度
	public Segment<Integer> latitude11; //纬度 分
	public Segment<Integer> latitude20; //纬度  秒1 
	public Segment<Integer> latitude21; //纬度  秒2
	public Segment<Integer> eow;// 东西经度标示
	public Segment<Integer> longitude10; //经度 度
	public Segment<Integer> longitude11; //经度 分
	public Segment<Integer> longitude20; //经度 秒1
	public Segment<Integer> longitude21; //经度 秒2
	public Segment<Integer> altitude; //高度
	public Segment<Integer> velocity; //速度
	public Segment<Integer> orientation; //方向
	public Segment<Integer> mileage0; //累计里程1
	public Segment<Integer> mileage1; //累计里程2
	public Segment<Integer> mileage2; //累计里程3
	public Segment<Integer> mileage3; //累计里程3
	public Segment<String> time; //时间
	public Segment<String> warning; //报警状态
	//public Segment<Integer> before; //上一站点编号
	//skip(5);
	public Segment<String> driverID;
	public Segment<String> psw;
	
	private int s=0; //开始位置  //测试时改为6
	
	
	public LZBus0x89(ByteBuf bb){
		bb.skipBytes(s);
		validate   =new Segment<Integer>(bb,1,new IntegerParse());
		nos        =new Segment<Integer>(bb,1,new IntegerParse());
		latitude10 =new Segment<Integer>(bb,1,new IntegerParse());
		latitude11 =new Segment<Integer>(bb,1,new IntegerParse());
		latitude20  =new Segment<Integer>(bb,1,new IntegerParse());
		latitude21  =new Segment<Integer>(bb,1,new IntegerParse());
		eow        =new Segment<Integer>(bb,1,new IntegerParse());
		longitude10=new Segment<Integer>(bb,1,new IntegerParse());
		longitude11=new Segment<Integer>(bb,1,new IntegerParse());
		longitude20 =new Segment<Integer>(bb,1,new IntegerParse());
		longitude21 =new Segment<Integer>(bb,1,new IntegerParse());
		altitude   =new Segment<Integer>(bb,2,new IntegerParse());
		velocity   =new Segment<Integer>(bb,1,new IntegerParse());
		orientation=new Segment<Integer>(bb,2,new IntegerParse());
		mileage0   =new Segment<Integer>(bb,1,new IntegerParse());
		mileage1   =new Segment<Integer>(bb,1,new IntegerParse());
		mileage2   =new Segment<Integer>(bb,1,new IntegerParse());
		mileage3   =new Segment<Integer>(bb,1,new IntegerParse());
		time	   =new Segment<String>(bb,6,new DateTimeParse());
		warning    =new Segment<String>(bb,4,new StringParse());
		//before     =new Segment<Integer>(bb,2,new IntegerParse());
		bb.skipBytes(5);
		driverID=new Segment<String>(bb,10,new StringParse());
		psw=new Segment<String>(bb,10,new StringParse());
	}
	
	public LZBusDriver convert(){
		LZBusDriver driver=new LZBusDriver();
		JT0x0200 jt=null;
		//if(validate.value==0x56) return null;
		jt=new JT0x0200();
		jt.latitude=compute(latitude10.value,latitude11.value,latitude20.value,latitude21.value);
		jt.longitude=compute(longitude10.value,longitude11.value,longitude20.value,longitude21.value);;
		jt.altitude=altitude.value;
		jt.velocity=velocity.value;
		jt.orientation=orientation.value;
		//累计里程 累计里程=A*25600 + B*100 + C + D/100
		jt.mileage=mileage0.value*25600+mileage1.value*100+mileage2.value+mileage3.value/100.0;
		jt.time="20"+time.value;
		//报警信息
		//String s=body.substring(52, 60);  
		//log.info(body.substring(60,64));
		//String before=Ecode.HEX2DEC(body.substring(60,64));//上一站点编号
		//td.extID=Integer.parseInt(before);
		
		String state=Ecode.HEX2DECLH(warning.value);
		String bin=Ecode.decToBin(Long.parseLong(state));
		
		while(bin.length()<32){
			bin="0"+bin;
		}
		//log.debug("bin:"+bin);
		StringBuffer st=new StringBuffer("00000000000000000000000000000000");
		StringBuffer wa=new StringBuffer("00000000000000000000000000000000");
		boolean old=false;  //补传数据标识 false 实时  true 补传数据
		for(int i=0;i<bin.length();i++){
			switch(i){
				case 0:
					if(bin.charAt(31-i)=='1') st.replace(31, 32, "1"); break;  //ok ACC     
				case 1:
					if(bin.charAt(31-i)=='1') {
						wa.replace(30, 31, "1");
						//log.info("超速 ***"+jt.velocity);
					}break;  //超速
				case 2:
					 break;
				case 3:
					if(bin.charAt(31-i)=='1') wa.replace(12, 13, "1"); break;  //停留超时　
				case 4:
					if(bin.charAt(31-i)=='1') wa.replace(31, 32, "1"); break;  //用户按键 重要  ok
				case 5:
					if(bin.charAt(31-i)=='1') wa.replace(25, 26, "1"); break;
				case 6:
					if(bin.charAt(31-i)=='1') wa.replace(26, 27, "1"); break;
				case 7:
					//if(bin.charAt(31-i)=='1') wa.replace(27, 28, "1"); break;
					if(bin.charAt(31-i)=='1') wa.replace(19, 20, "1"); break;
				case 8:
					break;
				case 9:
					if(bin.charAt(31-i)=='1'){
						st.replace(30, 31, "1");   //未定位　GPS无效  ok
					}else{
						wa.replace(26, 27, "1");  //GNSS天线未接或断路
					}
					break;
				case 10:
					if(bin.charAt(31-i)=='1') st.replace(18, 19, "1"); break;  //1 营运中
				case 11:
					if(bin.charAt(31-i)=='1') st.replace(17, 18, "1"); break;  //1 上行
				case 12:
					break;
				case 13:
					 break;
				case 14:
					break;
				case 15:
					 break;
				case 16:
					break;
				case 17:
					break;
				case 18:
					break;
				case 19:
					if(bin.charAt(31-i)=='1') st.replace(25, 26, "1"); break;  //重车 1  空车 0   st 6位
				case 20:
					if(bin.charAt(31-i)=='1') wa.replace(8, 9, "1"); break;  //越界报警
				case 21:
					break;
				case 22:
					break;
				case 23:
					if(bin.charAt(31-i)=='1'){ st.replace(24, 25, "1");old=true;} break;  //补传GPS数据    ok  //st 7位
				case 24:
					break;
				case 25:
					break;
				case 26:
					break;
				case 27:
					break;
				case 28:
					//if(bin.charAt(31-i)=='1') wa.replace(29, 30, "1"); break;  //疲劳驾驶
				case 29:
					break;
				case 30:
					if(bin.charAt(31-i)=='1') st.replace(20, 21, "1"); break;  //车辆断电
				case 31:
					//if(bin.charAt(31-i)=='1') status.replace(31, 32, "1"); break;
			}
		}
		jt.status=Ecode.binaryString2hexString(st.toString());
		jt.warning=Ecode.binaryString2hexString(wa.toString());
		jt.iwarning=Long.valueOf(jt.warning,16);
		jt.istatus=Long.valueOf(jt.status,16);
		
		//2014-3-12新增
		long x=(long) Math.pow(2, 31)-3;  //屏蔽第2位(从右到左 数)
		if((jt.istatus&VStatus.NS[1])==0){//gps无效 ,清除超速标识
			wa.replace(30, 31, "0");//清除超速标识
			jt.iwarning=jt.iwarning&x;
		}
			wa.replace(30, 31, "0");
		if((jt.iwarning&VWarns.WS[4])==0){  //gps无效 ,清除超速标识
			wa.replace(30, 31, "0");//清除超速标识
			jt.iwarning=jt.iwarning&x;

		}
		if((jt.iwarning&VWarns.WS[5])==0){  //gps无效 ,清除超速标识
			wa.replace(30, 31, "0");//清除超速标识
			jt.iwarning=jt.iwarning&x;
		}
		if((jt.iwarning&VWarns.WS[6])==0){  //gps无效 ,清除超速标识
			wa.replace(30, 31, "0");//清除超速标识
			jt.iwarning=jt.iwarning&x;
		}
		if(jt.velocity<46){  //最后保障
			wa.replace(30, 31, "0");//清除超速标识
			jt.iwarning=jt.iwarning&x;
		}
		driver.pos=jt;
		driver.driverID=Ecode.A2S(driverID.value).trim();
		driver.psw=Ecode.A2S(psw.value).trim();
		return driver;
	}
	private double compute(int d,int m,int s1,int s2){
		//return (bs[8]+((bs[9]+ bs[10]/100.0 + bs[11]/10000.0)/60.0))
		//System.out.println(d+"   "+m+"  "+s1/100.0+"   "+s2/10000.0);
		//System.out.println((d+    ((m+s1/100.0+s2/10000.0)/60.0)));
		/*
		DecimalFormat df = new DecimalFormat("#.000000");
        return   Double.parseDouble(df.format(d+((m+s1/100.0+s2/10000.0)/60.0)));*/
		return  (d+((m+s1/100.0+s2/10000.0)/60.0));
	}
	public static void main(String args[]){

		
		LinkedList<String> ls=FileUtil.readFile("d:\\gpslog\\lzbus0x41.txt");
		
		for(Iterator<String> it=ls.iterator();it.hasNext();){
			String s=it.next();
			byte bs[]= new byte[s.length()/2];
			bs=Ecode.HexString2ByteArray(s);
			
			ByteBuf bb=Unpooled.buffer(bs.length);
			for(int i=0;i<bs.length;i++){
				bb.writeByte(bs[i]);
			}
			LZBus0x41 t=new LZBus0x41(bb);
			JT0x0200 jt=(JT0x0200)(t.convert());
			log.info(jt);
		}
		/*
		long x=(long) Math.pow(2, 31)-3;
		long y=(long) Math.pow(2, 31)-1;
		System.out.println(Ecode.decToBin(x));
		System.out.println(Ecode.decToBin(y));
		System.out.println(Ecode.decToBin(x&y));
		System.out.println(x&y);*/
	}
}