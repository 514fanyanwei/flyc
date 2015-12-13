package cc.gps.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.jt808.JT0x0102;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JT0x9999;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.util.SerializeUtil;
import cc.util.db.DataSourceUtil;

//public class SaveObjTask extends Task{ //自定义线程池的要求
public class SaveObjTask extends Thread{
	private static final Log log = LogFactory.getLog(SaveObjTask.class);
	//TableCtrl tc = new TableCtrl();
	JTReceiveData obj;
	public SaveObjTask(JTReceiveData object){
		 this.obj=(JTReceiveData)object;
		 //log.info(obj.head.messageID+"***********"+obj.head.serialID+"  "+obj);
		 
	}
	public void run() {
		Connection con=DataSourceUtil.getConn();
		//Connection con=tc.getConn();
		String insertSQL="insert into rgps(rid,saveTime,messageID,serialID,clientID,obj) values(?,?,?,?,?,?)";
		PreparedStatement pst=null;
		
		//log.info(this.obj.head.messageID+"***||||||||********"+obj.head.serialID+"   "+obj);
		String uid=UUID.randomUUID().toString();
		try{
			pst=con.prepareStatement(insertSQL);
			pst.setString(1, uid);
			pst.setString(2, new Date().toLocaleString());
			pst.setInt(3, obj.head.messageID);
			pst.setLong(4,obj.head.serialID);
			pst.setString(5,obj.head.phone);
			pst.setBytes(6, SerializeUtil.serializeObject(obj));
			pst.executeUpdate();
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
				if(pst!=null) pst.close();
				con.close();
			}catch(Exception e2){e2.printStackTrace();}
		}
	}
		
/*		

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
	@Override
	public Task[] taskCore() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	public static void main(String args[]){
		//TableCtrl tc = new TableCtrl();
		//Connection con=tc.getConn();
		Connection con=DataSourceUtil.getConn();
		String sql="select top 10 rid,saveTime,messageID,clientID,obj from rgps";
		PreparedStatement pst=null;
		try{
			pst=con.prepareStatement(sql);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				int messageID=rs.getInt("messageID");
				switch(messageID){
				case 258:
					JT0x0102 obj=(JT0x0102)SerializeUtil.deserializeObject(rs.getBytes("obj"));
					log.info(obj.body+" ** "+obj.head.len);
					break;
				case 512:
					JT0x0200 obj2=(JT0x0200)SerializeUtil.deserializeObject(rs.getBytes("obj"));
					log.info(obj2.time+" "+obj2.body+" ** "+obj2.head.len+"  "+obj2.latitude+"  "+obj2.longitude);
					break;
				case 39321:
					JT0x9999 obj3=(JT0x9999)SerializeUtil.deserializeObject(rs.getBytes("obj"));
					//log.info(obj3.head.phone+"******");
					break;
				}
				
			}
		}catch(Exception e){e.printStackTrace();}
		finally{
			try{
				if(pst!=null) pst.close();
				con.close();
			}catch(Exception e2){e2.printStackTrace();}
		}
	}*/
}