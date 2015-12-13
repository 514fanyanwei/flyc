package cc.gps.parse.lztaxi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.jt808.VWarns;
import cc.gps.data.lztaxi.LZTAXIFee;
import cc.gps.parse.DateTimeParse;
import cc.gps.parse.IntegerParse;
import cc.gps.parse.LongParse;
import cc.gps.parse.Segment;
import cc.gps.parse.StringParse;
import cc.gps.parse.lzbus.LZBus0x41;
import cc.gps.util.Ecode;
import cc.gps.util.FileUtil;

public class LZTAXI0x7f {
	private static final Log log = LogFactory.getLog(LZTAXI0x7f.class);
	//预留 2个字符
	public Segment<Integer> fid; //功能字   2
	public NanJing nj;
	
	private int s=0; //开始位置  //测试时改为6
	
	
	public LZTAXI0x7f(ByteBuf bb){
		bb.skipBytes(s);
		bb.skipBytes(2);//跳过预留2个字符
		fid   =new Segment<Integer>(bb,2,new IntegerParse());
		switch(fid.value){
			case 0x0000:   break;
			case 0x0006:
			case 0x00E8:   nj=new C0006(bb);log.info(nj);break;
			case 0x00E7:   nj=new C00E7(bb);log.info(nj);break;
			case 0xF0E1:   nj=new CF0E1(bb);log.info(nj);break;      // -计价器发送签到命令
			case 0xF0E4:      // -计价器发送签退命令
			case 0xF0F1:   nj=new CF0E4(bb);log.info(nj);break;     //-计价器补发送签退数据命令
		}
	}
	
	public JTReceiveData convert(){
		JTReceiveData data=null;
		switch(fid.value){
			case 0x0000:   break; //0x0000-计价器状态查询应答
			
			case 0x0006: //0x0006-营运记录查询应答
			case 0x00E8: //0x00E8-单次营运结束后营运数据发送指令
			case 0x00F2: //0x00F2-计价器营运数据补传指令
				data=pe8((C0006)nj);break;	
			
			case 0x00E7:   data=pe7((C00E7)nj);break;  //0x00E7-单次营运开始通知指令
			
			case 0xF0E1:         // -计价器发送签到命令
			case 0xF0E4:      // -计价器发送签退命令
			case 0xF0F1:        //-计价器补发送签退数据命令
		}
		return data;
	}
	private JTReceiveData pe8(C0006 nj){
		LZTAXIFee lz= new LZTAXIFee();
		if((nj==null)||(nj.vno==null)) return null; //若为空，则直接返回
		lz.vno     =Ecode.A2S(nj.vno.value).trim();
		lz.dwdm    =Ecode.A2S(nj.dwdm.value).trim();    
		lz.licence =Ecode.A2S(nj.licence.value).trim(); 
		lz.onTime  =nj.onTime.value.substring(0,2)+"-"+nj.onTime.value.substring(2,4)+"-"+nj.onTime.value.substring(4,6)+" "+nj.onTime.value.substring(6,8)+":"+nj.onTime.value.substring(8,10)+":00";  
		lz.offTime =nj.onTime.value.substring(0,2)+"-"+nj.onTime.value.substring(2,4)+"-"+nj.onTime.value.substring(4,6)+" "+nj.offTime.value.substring(0,2)+":"+nj.offTime.value.substring(2,4)+":00";; 
		//lz.miles   =Integer.parseInt(Ecode.BCD2DEC(nj.miles.value))/10;   
		lz.miles   =Integer.parseInt(nj.miles.value)/10.0;
		//lz.emiles  =Integer.parseInt(Ecode.BCD2DEC(nj.emiles.value))/10;
		lz.emiles  =Integer.parseInt(nj.emiles.value)/10.0;
		//lz.eFee    =Integer.parseInt(Ecode.BCD2DEC(nj.eFee.value))/10;
		lz.eFee    =Integer.parseInt(nj.eFee.value)/10.0;    
		lz.waitTime=Integer.parseInt(nj.waitTime.value.substring(0,2))*60+Integer.parseInt(nj.waitTime.value.substring(2,4)); 
		//lz.fee     =Integer.parseInt(Ecode.BCD2DEC(nj.fee.value))/10;     
		lz.fee     =Integer.parseInt(nj.fee.value)/10.0;
		lz.cNO     =nj.cNO.value;     
		lz.fid=this.fid.value;

		return lz;
	}
	private JTReceiveData pe7(C00E7 nj){
		LZTAXIFee lz= new LZTAXIFee();
		log.info("进入重车状态");

		return null;
	}
	
	//----------------
	class NanJing{ }
	class C0000 extends NanJing{  //计价器状态查询应答
		//预留 2个字符
		public Segment<String> did; //设备编号   5
		public Segment<String> dHV; //设备硬件版本号   1
		public Segment<String> dMV; //设备主版本号   1
		public Segment<String> dSV; //设备次版本号   1
		public Segment<String> status; //设备状态   1
		public Segment<String> gzstatus; //计价器工作状态   1
		public Segment<String> vno; //车牌号 6
		public Segment<String> dwdm;//单位代码	16
		public Segment<String> licence;//驾驶员从业资格证号	19
		public Segment<LongParse> all;//总营运次数	4
	}
	class C00E7 extends NanJing{  //单次营运开始通知指令
		public Segment<String>  start; //进入重车时间	7	BCD 格式YYYY-MM-DD-hh-mm-ss
		public C00E7(ByteBuf bb){
			start   =new Segment<String>(bb,7,new StringParse());
		}
		public String toString(){
			return "上车时间:"+start.value;
		}
	}
	
	class C0006 extends NanJing{  //营运记录查询应答   //0x00E8-单次营运结束后营运数据发送指令 
		public Segment<String> vno; //车牌号	6	车牌号，ASCII字符
		public Segment<String> dwdm; //单位代码	16	ASCII 不足16位右补0x00
		public Segment<String> licence; //驾驶员从业资格证号	19	ASCII IC卡从业资格证号为身份证号，不足19位右补0x00
		public Segment<String> onTime; //上车时间	5	BCD	格式为YY-MM-DD-hh-mm
		public Segment<String> offTime; //下车时间	2	BCD格式为hh-mm
		public Segment<String> miles; //计程公里	3	BCD格式为XXXXX.X公里
		public Segment<String> emiles; //空驶公里	2	BCD格式为XXX.X公里
		public Segment<String> eFee; //附加费	3	BCD格式XXXXX.X元
		public Segment<String> waitTime; //等待计时时间	2	BCD格式为hh-mm
		public Segment<String> fee; //交易金额	3	BCD格式XXXXX.X元
		public Segment<Integer> cNO; //当前车次	4	高位在前，低位在后
		//以下为一卡通交易数据项（当交易类型为现金交易时，以下数据项以0x00填充）
		public Segment<Integer> cardtype; //交易卡类型	1	0x00：现金交易；0x01:M1卡交易；0x03：CPU卡交易
		public Segment<String> cardNO; //交易卡号	8	BCD
		public Segment<String> psmaNO; //PSMA卡终端编码	6	BCD PSAM卡0016文件中
		public Segment<String> psmaSerial; //PSAM卡终端交易流水号	4	高位在前，低位在后
		public Segment<String> cityNO; //城市代码	2	BCD
		public Segment<String> cardM; //卡主类型	1	BCD
		public Segment<String> cardS; //卡子类型	1	BCD 当交易卡类型为M1卡时，该字节填充00
		public Segment<String> balance; //交易前余额	3	BCD 格式为XXXX.XX元
		public Segment<String> deduct; //扣款金额	3	BCD格式XXXXX.X元
		public Segment<String> tac; //消费TAC码	4	HEX
		public Segment<String> cardCounter; //卡交易计数器	2	HEX 高位在前，低位在后
		public Segment<String> cardACCode; //卡认证码	4	HEX 当交易卡类型为M1卡时有效，其余填充0x00
		
		public C0006(ByteBuf bb){
			vno    =new Segment<String>(bb,6,new StringParse());
			dwdm   =new Segment<String>(bb,16,new StringParse());
			licence=new Segment<String>(bb,19,new StringParse());
			onTime =new Segment<String>(bb,5,new StringParse());
			offTime=new Segment<String>(bb,2,new StringParse());
			miles  =new Segment<String>(bb,3,new StringParse());
			emiles =new Segment<String>(bb,2,new StringParse());
			eFee   =new Segment<String>(bb,3,new StringParse());
			waitTime=new Segment<String>(bb,2,new StringParse());
			fee    =new Segment<String>(bb,3,new StringParse());
			cNO    =new Segment<Integer>(bb,4,new IntegerParse());
		}
		public String toString(){
			return "车牌号	6	车牌号                     "+vno.value+"\n"+
					"单位代码	16	          "+dwdm.value+"\n"+ 
					"驾驶员从业资格证号	19	  "+licence.value+"\n"+ 
					"上车时间	5	BCD	格式为YY-MM-DD-hh-mm "+onTime.value+"\n"+ 
					"下车时间	2	BCD格式为hh-mm           "+offTime.value+"\n"+ 
					"计程公里	3	BCD格式为XXXXX.X公里     "+miles.value+"\n"+ 
					"空驶公里	2	BCD格式为XXX.X公里       "+emiles.value+"\n"+ 
					"附加费	3	BCD格式XXXXX.X元           "+eFee.value+"\n"+ 
					"等待计时时间	2	BCD格式为hh-mm       "+waitTime.value+"\n"+ 
					"交易金额	3	BCD格式XXXXX.X元         "+fee.value+"\n"+ 
					"当前车次	4	高位在前，低位在后       "+cNO.value+"\n"; 

		}
	}
	
	
	class CF0E1 extends NanJing{  //计价器发送签到命令
		public Segment<String> dwdm;//单位代码	8	ASCII码 从驾驶员的卡上读取
		public Segment<String> cddm;//车队代码	4	ASCII码 从驾驶员的卡上读取
		//public Segment<String> dwdm;//保留	4	从驾驶员的卡上读取目前为0
		public Segment<String> licence;//驾驶员从业资格证号	19	ASCII码从驾驶员的卡上读取的驾驶员的身份证号，不足后补0
		public Segment<String> vno;//车牌号	6	ASCII
		public Segment<String> startTime;//计价器开机时间	6	BCD码格式为YYYY-MM-DD-hh-mm
		public Segment<Long> all;//总运营次数	4	高位在前，低位在后
		public Segment<Integer> result;//操作结果	1	0x90:执行正确;0xFF:执行错误
		
		public CF0E1(ByteBuf bb){
			dwdm   =new Segment<String>(bb,8,new StringParse());
			cddm   =new Segment<String>(bb,4,new StringParse());
			bb.skipBytes(4);//保留	4	从驾驶员的卡上读取目前为0
			licence = new Segment<String>(bb,19,new StringParse());
			vno =new Segment<String>(bb,6,new StringParse());
			startTime=new Segment<String>(bb,6,new StringParse());
			all  =new Segment<Long>(bb,4,new LongParse());
			result    =new Segment<Integer>(bb,1,new IntegerParse());
		}

	}
	
	class CF0E4 extends NanJing{  //计价器发送签退命令
		public Segment<String> dwdm;//单位代码	8	ASCII码 从驾驶员的卡上读取
		public Segment<String> cddm;//车队代码	4	ASCII码 从驾驶员的卡上读取
		//保留	4	从驾驶员的卡上读取目前为0
		public Segment<String> licence;//驾驶员从业资格证号	19	ASCII码从驾驶员的卡上读取的驾驶员的身份证号，不足后补0
		public Segment<String> vno;//车牌号	6	ASCII
		public Segment<String> KV;//K值	2	BCD 格式为XXXX，最大9999
		public Segment<String> startTime;//当班开机时间	6	BCD 格式为YYYY-MM-DD-hh-mm
		public Segment<String> closeTime;//当班关机时间	6	BCD 格式为YYYY-MM-DD-hh-mm
		public Segment<String> miles;//当班公里	3	BCD 格式为XXXXX.X公里
		public Segment<String> wmiles;//当班营运公里	3	BCD 格式为XXXXX.X公里
		public Segment<String> cNO;//车次	2	BCD 格式为XXXX，最大9999
		public Segment<String> allTime;//计时时间	3	BCD
		public Segment<String> allFee;//总计金额	3	BCD 格式XXXXX.X元
		public Segment<String> cardFee;//卡收金额	3	BCD 格式XXXXX.X元
		public Segment<String> cardCount;//卡次	2	BCD 格式为XXXX，最大9999
		public Segment<String> bjMiles;//班间公里	2	BCD 格式XXX.X公里（上一班签退到本班签到的公里数）
		public Segment<String> allMiles;//总计公里	4	BCD 格式为XXXXXXX.X公里（计价器安装后累积）
		public Segment<String> allWMiles;//总营运公里	4	BCD 格式为XXXXXXX.X公里（计价器安装后累积）
		public Segment<String> dj;//单价	2	BCD 格式XX.XX元
		public Segment<Integer> allCount;//总营运次数	4	高位在前，低位在后
		public Segment<Integer> offType;//签退方式	1	0-正常签退 1-强制签退
		
		public CF0E4(ByteBuf bb){
			dwdm   =new Segment<String>(bb,8,new StringParse());
			cddm   =new Segment<String>(bb,4,new StringParse());
			bb.skipBytes(4);//保留	4	从驾驶员的卡上读取目前为0
			licence = new Segment<String>(bb,19,new StringParse());
			vno =new Segment<String>(bb,6,new StringParse());
			KV=new Segment<String>(bb,2,new StringParse());
			startTime=new Segment<String>(bb,6,new StringParse());
			closeTime=new Segment<String>(bb,6,new StringParse());
			miles=new Segment<String>(bb,6,new StringParse());
			wmiles  =new Segment<String>(bb,3,new StringParse());
			cNO  =new Segment<String>(bb,2,new StringParse());
			allTime  =new Segment<String>(bb,3,new StringParse());
			allFee  =new Segment<String>(bb,3,new StringParse());
			cardFee  =new Segment<String>(bb,3,new StringParse());
			cardCount  =new Segment<String>(bb,2,new StringParse());
			bjMiles  =new Segment<String>(bb,2,new StringParse());
			allMiles  =new Segment<String>(bb,4,new StringParse());
			allWMiles  =new Segment<String>(bb,4,new StringParse());
			dj  =new Segment<String>(bb,2,new StringParse());
			allCount  =new Segment<Integer>(bb,4,new IntegerParse());
			offType    =new Segment<Integer>(bb,1,new IntegerParse());
		}
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
	}
}
