package cc.gps.thread;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.config.Global;

import cc.gps.data.jt808.C2XAnswer;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JT0x0801;
import cc.gps.data.jt808.JT0x9999;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VStatus;
import cc.gps.data.jt808.VWarns;
import cc.gps.data.lzbus.LZBusDriver;
import cc.gps.data.lzbus.LZBusDriverRequest;
import cc.gps.data.lzbus.LZBusStation;
import cc.gps.data.lztaxi.LZTAXIFee;
import cc.gps.util.DateUtil;
import cc.gps.util.Ecode;
import cc.util.db.DataSourceUtil;


public class Save2DBTask extends Task{
	private static final Log log = LogFactory.getLog(Save2DBTask.class);
	
	JTReceiveData data_old;
	public Save2DBTask(JTReceiveData data_old){
		 this.data_old=data_old;
	}
	public void run() {
		if (data_old instanceof LZBusStation){
			JT0x0200 data1 = ((LZBusStation)data_old).pos;
			saveGPSInfo(data1);
			LZBusStation data2 = (LZBusStation)data_old;
			saveGPSStation(data2);
			return;
		}
		
		if (data_old instanceof JT0x0200){
			JT0x0200 data = (JT0x0200)data_old;
			saveGPSInfo(data);
			return ;
		}
		if (data_old instanceof JT0x0102){
			JT0x0102 data = (JT0x0102)data_old;
			saveGPSEvent(data,"login",data.body);
			return ;
		}
		if (data_old instanceof JT0x9999){
			JT0x9999 data = (JT0x9999)data_old;
			saveGPSEvent(data,"logout","");
			return ;
		}
		if (data_old instanceof C2XAnswer){
			C2XAnswer data = (C2XAnswer)data_old;
			if(data.attachment!=null)
				saveGPSEvent(data,"send Fail",data.attachment.body);
			return ;
		}
		
		
		if (data_old instanceof LZBusDriver){
			LZBusDriver data1 = (LZBusDriver)data_old;
			saveGPSDriver(data1);
			return;
		}
		
		if (data_old instanceof LZBusDriverRequest){
			LZBusDriverRequest data1 = (LZBusDriverRequest)data_old;
			saveDriverStatus(data1);
			return;
		}
		if (data_old instanceof JT0x0801)
		{
			JT0x0801 jt = (JT0x0801)data_old;
			saveImage(jt);
			return;
		}
		if (data_old instanceof LZTAXIFee)
		{
			LZTAXIFee fee = (LZTAXIFee)data_old;
			saveFlTaxiFee(fee);
			return;
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
		//立即执行
		return true;
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private void saveGPSInfo(JT0x0200 data){
		Connection con=DataSourceUtil.getConn();
		String insertSQL="insert into GpsReceRealTime(gpsid,telephone,gpstime,recetime,longitude,latitude,direct,speed,altitude,carstatu,alarminfo,distancediff) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		String delSQL="delete from GpsReceRealTime2 where telephone=?";
		String insertSQL2="insert into GpsReceRealTime2(gpsid,telephone,gpstime,recetime,longitude,latitude,direct,speed,altitude,carstatu,alarminfo,distancediff) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		
		
		String uid=UUID.randomUUID().toString();
		try{
			pst=con.prepareStatement(insertSQL);
			pst.setString(1, uid);
			pst.setString(2, "86"+data.head.phone.substring(1));
			pst.setString(3, data.time);
			pst.setString(4, new Date().toLocaleString());
			pst.setDouble(5,  data.longitude);
			pst.setDouble(6, data.latitude);
			pst.setDouble(7, data.orientation);
			pst.setDouble(8, data.velocity);
			pst.setDouble(9, data.altitude);
			pst.setLong(10,  data.istatus);
			pst.setLong(11,  data.iwarning);
			pst.setDouble(12,  data.mileage);
			pst.executeUpdate();
			
			
			//更新当前最新位置
			if((data.istatus&VStatus.NS[7])==0){
				//log.info("save2dbTask:最新数据");
				//log.info(data);
				pst2=con.prepareStatement(delSQL);
				pst2.setString(1, "86"+data.head.phone.substring(1));
				pst2.executeUpdate();
				
				pst2=con.prepareStatement(insertSQL2);
				pst2.setString(1, uid);
				pst2.setString(2, "86"+data.head.phone.substring(1));
				pst2.setString(3, data.time);
				pst2.setString(4, new Date().toLocaleString());
				pst2.setDouble(5,  data.longitude);
				pst2.setDouble(6, data.latitude);
				pst2.setDouble(7, data.orientation);
				pst2.setDouble(8, data.velocity);
				pst2.setDouble(9, data.altitude);
				pst2.setLong(10,  data.istatus);
				pst2.setLong(11,  data.iwarning);
				pst2.setDouble(12,  data.mileage);
				pst2.executeUpdate();
			}else{
				log.debug("save2dbTask:补传数据");
			}
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
				if(pst!=null) pst.close();
				if(pst2!=null) pst2.close();
				con.close();
			}catch(Exception e2){e2.printStackTrace();}
		}
	}
	
	private void saveGPSStation(LZBusStation data){
		/*
		sid,veno,linesid,linename,simno,positiontype,stationid,direction,logitude,latitude,runningtype,curspeed,ljmiles,gpstime,recvtime,isparse,driverno,drivername
		*/
		if(((data.pos.istatus&VStatus.NS[1])==0)||((data.pos.istatus&VStatus.NS[7])!=0)) return;
		Connection con=DataSourceUtil.getConn();
		String insertSQL="insert into BUS_GPS_DATA_temp(sid,simno,positiontype,stationid,direction,logitude,latitude," +
				"curspeed,ljmiles,gpstime,recvtime,inorout,runningtype,driverno,linenum,areaID) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		//driverno,drivername
		PreparedStatement pst=null;
		
		String uid=UUID.randomUUID().toString();
		try{
			pst=con.prepareStatement(insertSQL);
			pst.setString(1, uid);
			pst.setString(2, "86"+data.pos.head.phone.substring(1));
			pst.setString(3, data.stationType);
			pst.setInt(4, data.stationID);
			int up=0; //下行
			if((data.pos.istatus&VStatus.NS[14])!=0)	up=1;  //上行
				
			pst.setInt(5,up);
			pst.setDouble(6, data.pos.longitude);
			pst.setDouble(7, data.pos.latitude);
			pst.setDouble(8, data.pos.velocity);
			pst.setDouble(9, data.pos.mileage);
			pst.setString(10, data.pos.time);
			pst.setString(11, new Date().toLocaleString());
			pst.setInt(12,data.inout);
			int runningtype=1;//营运
			if((data.pos.istatus&VStatus.NS[13])==0)	runningtype=0;  //非营运
			pst.setInt(13,runningtype);
			pst.setString(14,Global.CID2Driver.get(data.pos.head.phone));
			String lineNum=Global.CID2Line.get(data.pos.head.phone);
			pst.setString(15,lineNum);
			pst.setInt(16, data.areaID);
			/*
			log.info(data.pos);
			log.info("***********");
			log.info(data.pos.head.phone);
			log.info(Global.CID2Driver.get(data.pos.head.phone));
			log.info(Global.CID2Line.get(data.pos.head.phone));
			log.info(data.pos.head.phone);
			log.info(lineNum);*/
			pst.executeUpdate();
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
				if(pst!=null) pst.close();
				con.close();
			}catch(Exception e2){e2.printStackTrace();}
		}
	}

	
	private void saveGPSDriver(LZBusDriver driverData){
		/*
		sid,veno,linesid,linename,simno,positiontype,stationid,direction,logitude,latitude,runningtype,curspeed,ljmiles,gpstime,recvtime,isparse,driverno,drivername
		*/
		saveGPSInfo(driverData.pos);
		JT0x0200 data=driverData.pos;
		Connection con=DataSourceUtil.getConn();
		String insertSQL="insert into DriverLog(gpsid,telephone,gpstime,recetime,longitude,latitude,direct,speed,altitude,carstatu,alarminfo,distancediff,DriverNO,DriverPSW,StationID,login) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst=null;
		
		String uid=UUID.randomUUID().toString();
		try{
			pst=con.prepareStatement(insertSQL);
			pst.setString(1, uid);
			pst.setString(2, "86"+data.head.phone.substring(1));
			//pst.setString(2, "86"+driverData.head.phone.substring(1));
			pst.setString(3, data.time);
			pst.setString(4, new Date().toLocaleString());
			pst.setDouble(5,  data.longitude);
			pst.setDouble(6, data.latitude);
			pst.setDouble(7, data.orientation);
			pst.setDouble(8, data.velocity);
			pst.setDouble(9, data.altitude);
			pst.setLong(10,  data.istatus);
			pst.setLong(11,  data.iwarning);
			pst.setDouble(12,  data.mileage);
			pst.setString(13, driverData.driverID);
			pst.setString(14, driverData.psw);
			pst.setInt(15, driverData.stationID);
			pst.setInt(16, driverData.login);
			pst.executeUpdate();
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
				if(pst!=null) pst.close();
				con.close();
			}catch(Exception e2){e2.printStackTrace();}
		}
	}
	
	private void saveDriverStatus(LZBusDriverRequest driverData){
		/*
		sid,veno,linesid,linename,simno,positiontype,stationid,direction,logitude,latitude,runningtype,curspeed,ljmiles,gpstime,recvtime,isparse,driverno,drivername
		*/
		//System.out.println(driverData);
		JT0x0200 data=driverData.pos;
		if(data!=null){
			saveGPSInfo(driverData.pos);
		}
		//if((data!=null)&&(((data.istatus&VStatus.NS[1])==0)||((data.istatus&VStatus.NS[7])!=0))) return;
		//2014-5-7 打开GPS未定位的 报告
		//if((data!=null)&&((data.istatus&VStatus.NS[1])==0)) return;
		
		Connection con=DataSourceUtil.getConn();
		String insertSQL="insert into bus_gps_curstatus(vesid,simno,status,gpstime,mile,recetime,linenum,driverNo) values(?,?,?,?,?,?,?,?)";
		//String insertSQL="insert into bus_gps_curstatus(vesid,simno,status,gpstime,mile,recetime,linenum) values(?,?,?,?,?,?,?)";
		PreparedStatement pst=null;
		
		String uid=UUID.randomUUID().toString();
		try{
			pst=con.prepareStatement(insertSQL);
			pst.setString(1, uid);
			pst.setString(2, "86"+driverData.head.phone.substring(1));
			pst.setString(3, driverData.requestType);
			if(data!=null){
				pst.setString(4, data.time);
				pst.setDouble(5,  data.mileage);
			}else{
				pst.setString(4,new Date().toLocaleString());
				pst.setDouble(5, -1);
			}
			
			pst.setString(6, new Date().toLocaleString());
			pst.setString(7,Global.CID2Line.get(driverData.head.phone));
			//2014-5-5 append
			pst.setString(8,Global.CID2Driver.get(driverData.head.phone));
			//System.out.println("86"+driverData.head.phone.substring(1));
			//System.out.println(Global.CID2Driver.get(driverData.head.phone));
			pst.executeUpdate();
			
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
				if(pst!=null) pst.close();
				con.close();
			}catch(Exception e2){e2.printStackTrace();}
		}
	}
	
	private void saveGPSEvent(JTReceiveData obj,String event,String content){
		Connection con=DataSourceUtil.getConn();
		String insertSQL="insert into gpsevent(sid,sim,receivetime,event,content) values(?,?,?,?,?)";
		PreparedStatement pst=null;
		
		String uid=UUID.randomUUID().toString();
		try{
			pst=con.prepareStatement(insertSQL);
			pst.setString(1, uid);
			pst.setString(2, obj.head.phone.substring(1));
			pst.setString(3, new Date().toLocaleString());
			pst.setString(4, event);
			pst.setString(5, content);
			pst.executeUpdate();
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
				if(pst!=null) pst.close();
				con.close();
			}catch(Exception e2){e2.printStackTrace();}
		}
	}
	private void saveImage(JT0x0801 jt){
		//JT0x0801 jt = (JT0x0801)data_old;
		JT0x0200 data=null;
		if(jt.gps!=null)
		 data=(JT0x0200)(jt.gps);
		Hashtable ht = new Hashtable();
		String uid=UUID.randomUUID().toString();
		ht.put("gpsid", uid);
		ht.put("telephone",  "86"+jt.head.phone.substring(1));
		ht.put("recetime", new Date().toLocaleString());
		ht.put("gpstime", new Date().toLocaleString());
		String remove="";
		if(data!=null){  //JT/T808
			remove="starCondition,starNum,carCondition,receGisID,Reserved,TransportStatus,Accelerration,DutyStr,commflag,cameraID,AddMsgType,AddMsgTxt,DayFlag,picdatatype,ispic";
			ht.put("gpstime", data.time);
			ht.put("longitude", String.valueOf(data.longitude));
			ht.put("latitude", String.valueOf(data.latitude));
			ht.put("direct", String.valueOf(data.orientation));
			ht.put("speed",String.valueOf(data.velocity));
			ht.put("altitude",String.valueOf(data.altitude));
			ht.put("carstatu", String.valueOf(data.istatus));
			ht.put("alarminfo", String.valueOf(data.iwarning));
			ht.put("distancediff", String.valueOf(data.mileage));
		}else{   //蓝斯
			remove="starCondition,starNum,longitude,latitude,direct,speed,altitude,carstatu,alarminfo,distancediff,carCondition,receGisID,Reserved,TransportStatus,Accelerration,DutyStr,commflag,cameraID,AddMsgType,AddMsgTxt,DayFlag,picdatatype,ispic";
		}
		remove=remove.toLowerCase();
		//tc.insertRow("GpsReceRealTime",ht,remove);
		DataSourceUtil.insertRow("GpsReceRealTime",ht,remove);
			//多媒体数据
		String sql="update GpsReceRealTime set pic=?,picdatatype=? where gpsid='"+uid+"'";
		
		Connection con=DataSourceUtil.getConn();
		 PreparedStatement prestmt=null;
		try {
			prestmt = con.prepareStatement(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(jt.data.startsWith("0000"))
			jt.data=jt.data.substring(8);
		byte[] images=Ecode.HexString2ByteArray(jt.data);
		ByteArrayInputStream  is = new ByteArrayInputStream(images);
		//log.info(is.available()+"  "+is.toString()+"  "+images.length);
		is.reset();
        try {
        	prestmt.setBinaryStream(1,is,is.available());
        	prestmt.setInt(2, jt.mecode);
			prestmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				prestmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return ;
	}
	
	private void saveFlTaxiFee(LZTAXIFee td){
		//检测是否已记录
		String sql="select * from fltaxifee where sim=? and tnumber=? ";
		Connection con=DataSourceUtil.getConn();
		PreparedStatement prestmt=null;
		ResultSet rs=null;
		try {
			prestmt = con.prepareStatement(sql);
			prestmt.setString(1, td.head.phone);
			//prestmt.setString(2,td.offTime);
			prestmt.setLong(2,td.cNO);
			rs=prestmt.executeQuery();

			if(rs.next()){
				prestmt.close();
				con.close();
				//System.out.println("have");
				return; //已记录
			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sql="insert into fltaxifee(vno, dwdm, licence, ontime, offtime, valuationkm, emptykm, addition, waittime, fee, tnumber,rectime,sim) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try{
			//System.out.println(td.vno+" | "+td.dwdm+" | "+td.licence);
			prestmt = con.prepareStatement(sql);
			prestmt.setString(1, td.vno);
			prestmt.setString(2, td.dwdm);
			prestmt.setString(3, td.licence);
			prestmt.setString(4, td.onTime);
			prestmt.setString(5,td.offTime);
			prestmt.setDouble(6, td.miles);
			prestmt.setDouble(7, td.emiles);
			prestmt.setDouble(8, td.eFee);
			prestmt.setDouble(9, td.waitTime);
			prestmt.setDouble(10, td.fee);
			prestmt.setLong(11, td.cNO);
			prestmt.setString(12, new Date().toLocaleString());
			prestmt.setString(13, td.head.phone);
			prestmt.execute();
		}catch(Exception e){ e.printStackTrace();	 }
		finally{
			try{
				prestmt.close();
				con.close();
			}catch(Exception e1){
				e1.printStackTrace();
			}
			
		}	
	}
}


