package cc.gps.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VWarns;
import cc.util.db.DataSourceUtil;

public class SaveWarnTask extends Task{
	private static final Log log = LogFactory.getLog(Save2DBTask.class);
	
	JTReceiveData data_old;
	public SaveWarnTask(JTReceiveData data_old){
		 this.data_old=data_old;
	}
	public void run() {
		
		if (data_old instanceof JT0x0200){
			JT0x0200 data = (JT0x0200)data_old;
			String simnum="86"+data.head.phone.substring(1);
			String gpstime=data.time;
			String recetime= new Date().toLocaleString();
			//保存warn
			saveWarn(simnum,gpstime,recetime,data.iwarning);
			return ;
		}
	}
	
	@Override
	public Task[] taskCore() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean useDb() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean needExecuteImmediate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void saveWarn(String simnum,String gpstime,String recetime,long iwarn){
		//wid,simnum,gpstime,warnType,recetime
		Connection con=DataSourceUtil.getConn();
		String insertSQL="insert into gpswarn(wid,simnum,gpstime,warnType,recetime) values(?,?,?,?,?)";
		String delSQL="delete from gpswarn2 where simnum=?";
		String insertSQL2="insert into gpswarn2(wid,simnum,gpstime,warnType,recetime) values(?,?,?,?,?)";
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		
		StringBuffer info=new StringBuffer();

		for(int i=0;i<32;i++){
			if((iwarn&VWarns.WS[i])!=0){
				info.append(VWarns.WSINFO[i]+"|");
				
				//log.debug(VWarns.WSINFO[i]);
			}
		}
		try {
			if(info.length()==0){
				//log.info("无报警");
				//del from gpswarn2
				pst2=con.prepareStatement(delSQL);
				pst2.setString(1, simnum);
				pst2.executeUpdate();
				return;
			}
			String winfo[]=info.toString().split("\\|");
		   
			
			//报警记录
			//append gpswarn
			pst=con.prepareStatement(insertSQL);
			pst.setString(2, simnum);
			pst.setString(3, gpstime);
			pst.setString(5, recetime);
			
			//del from gpswarn2
			pst2=con.prepareStatement(delSQL);
			pst2.setString(1, simnum);
			pst2.executeUpdate();
			//append gpswarn2
			pst2=con.prepareStatement(insertSQL2);
			pst2.setString(2, simnum);
			pst2.setString(3, gpstime);
			pst2.setString(5, recetime);
			
			for(int i=0;i<winfo.length;i++){
				String uid=UUID.randomUUID().toString();
					
				pst.setString(1, uid);
				pst.setString(4, winfo[i]);
				pst.executeUpdate();

				pst2.setString(1, uid);
				pst2.setString(4, winfo[i]);
				pst2.executeUpdate();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(pst!=null) pst.close();
				if(pst2!=null) pst2.close();
				con.close();
			}catch(Exception e2){e2.printStackTrace();}
		}
		
		
		
		
		
		/*
		Hashtable ht = new Hashtable();
		
		
		ht.put("simnum",  simnum);
		ht.put("gpstime", gpstime);
		ht.put("recetime", recetime);*/
		/*
		StringBuffer info=new StringBuffer();
		for(int i=0;i<32;i++){
			if((iwarn&VWarns.WS[i])!=0){
				info.append(VWarns.WSINFO[i]);
				//log.debug(VWarns.WSINFO[i]);
			}
		}*/
		
		/*
		StringBuffer bin=new StringBuffer(Ecode.decToBin(iwarn));
		while(bin.length()<32) bin.insert(0, "0");
		
		//log.debug(bin);
		for(int i=0;i<bin.length();i++){
			switch(i){
				case 0:
					if(bin.charAt(31-i)=='1') info.append("紧急|"); break;  //ok ACC     
				case 1:
					if(bin.charAt(31-i)=='1') info.append("超速|"); break;  //超速
				case 2:
					if(bin.charAt(31-i)=='1') info.append("疲劳驾驶|"); break;  //疲劳 
				case 3:
					if(bin.charAt(31-i)=='1') info.append("预警|"); break;  //预警　
				case 4:
					if(bin.charAt(31-i)=='1') info.append("GNSS模块故障|"); break;  //GNSS故障
				case 5:
					if(bin.charAt(31-i)=='1') info.append("GNSS天线故障|"); break;  //GNSS天线故障
				case 6:
					if(bin.charAt(31-i)=='1') info.append("GNSS天线短路|"); break;  
				case 7:
					if(bin.charAt(31-i)=='1') info.append("终端主电源欠压|"); break;  
				case 8:
					if(bin.charAt(31-i)=='1') info.append("终端主电源掉电|"); break;
				case 9:
					if(bin.charAt(31-i)=='1') info.append("显示器故障|"); break;
				case 10:
					if(bin.charAt(31-i)=='1') info.append("TTS故障|"); break;
				case 11:
					if(bin.charAt(31-i)=='1') info.append("摄像头故障|"); break;
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
					if(bin.charAt(31-i)=='1') info.append("当天累计加驶超时|"); break;
				case 19:
					if(bin.charAt(31-i)=='1') info.append("超时停车|"); break;
				case 20:
					if(bin.charAt(31-i)=='1') info.append("进出区域|"); break;
				case 21:
					if(bin.charAt(31-i)=='1') info.append("进出路线|"); break;
				case 22:
					if(bin.charAt(31-i)=='1') info.append("路段行驶时间不足或过长|"); break;
				case 23:
					if(bin.charAt(31-i)=='1') info.append("路线偏离报警|"); break;
				case 24:
					if(bin.charAt(31-i)=='1') info.append("车辆VSS故障|"); break;
				case 25:
					if(bin.charAt(31-i)=='1') info.append("车辆油量异常|"); break;
				case 26:
					if(bin.charAt(31-i)=='1') info.append("车辆非法点火|"); break;
				case 27:
					if(bin.charAt(31-i)=='1') info.append("车辆非法位移|"); break;
				case 28:
					if(bin.charAt(31-i)=='1') info.append("碰撞侧翻报警|"); break;
				case 29:
					break;
				case 30:
					break;
				case 31:
					break;
			}
		}
		*/
		/*
		if(info.length()==0){
			//log.info("无报警");
			return;
		}
		String winfo[]=info.toString().split("\\|");
		//log.info(info.toString()+winfo.length);
		for(int i=0;i<winfo.length;i++){
			//log.info(winfo[i]);
			String uid=UUID.randomUUID().toString();
			ht.put("wid", uid);
			ht.put("warntype", winfo[i]);
			tc.insertRow("gpswarn",ht);;
		}*/
	}
	
	public static void main(String args[]){
		String w="超速|超速";
		String s[]=w.split("\\|");
		for(int i=0;i<s.length;i++){
			log.info(s[i]);
		}
	}
}
