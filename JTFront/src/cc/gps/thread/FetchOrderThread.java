package cc.gps.thread;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.config.Global;
import cc.gps.data.X2CData;
import cc.gps.util.Ecode;
import cc.gps.util.SerializeUtil;
import cc.util.db.DataSourceUtil;

public class FetchOrderThread extends Thread{
	private static final Log log = LogFactory.getLog(FetchOrderThread.class);
	//private TableCtrl tc = new TableCtrl();
	
	Connection con=DataSourceUtil.getConn();
	String sql0="select sid,obj from sgps where isSuccess=-3 order by sendTime";
	String sql1="update sgps set isSuccess=-2 where sid =?";
	String sql2="delete from sgps where serialID>=60000";
	PreparedStatement pst0=null;
	PreparedStatement pst1=null;
	PreparedStatement pst2=null;
	
	@Override
	public void run(){
		while(true){
			try {
				pst0=con.prepareStatement(sql0);
				pst1=con.prepareStatement(sql1);
				pst2=con.prepareStatement(sql2);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		
			//log.info("轮询待下发数据表");
			try{
				ResultSet rs=pst0.executeQuery();
				if(rs!=null){
					while((rs!=null)&&rs.next()){
						Object obj=(Object)SerializeUtil.deserializeObject(rs.getBytes("obj"));
						if(!(obj instanceof X2CData)) continue;
						X2CData x2c=(X2CData)obj;
						log.info("FetchOrderThread从数据表SGPS读取拟向"+x2c.getClientID()+" 下发 0x"+Ecode.DEC2HEX(x2c.getMessageID(),4)+"报文");
						//list.add(x2c);
						/*启多线程发送
						Task task = new SendOrderTask(x2c);
						Global.tp.addTask(task);*/
						//发送
						SendOrderTask sot=new SendOrderTask(x2c);
						sot.send();
						
						pst1.setString(1, rs.getString("sid"));
						pst1.addBatch();
					}
					pst1.executeBatch();
					pst2.executeUpdate();
				}
				sleep(10*1000);	
			}catch(Exception e){e.printStackTrace();}
			/*
			finally{
				try{
					pst0.close();
					pst1.close();
					pst2.close();
					con.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}*/
		}
	}



	
}
