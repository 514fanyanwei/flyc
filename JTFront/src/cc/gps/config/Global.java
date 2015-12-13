package cc.gps.config;
import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeKey;
import cc.gps.data.X2CData;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTDataAssemble;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.lzbus.LZBusImage;
import cc.gps.queue.GPSQueue;
import cc.gps.service.GPSLogObserver;
import cc.gps.service.GPSPosObserver;
import cc.gps.thread.CheckSendOrderThread;
import cc.gps.thread.FetchOrderThread;
import cc.gps.thread.Save2DBTaskPool;
import cc.gps.thread.SaveGPSPacketPool;
import cc.gps.thread.SaveObjTaskPool;
import cc.gps.thread.SaveWarnTaskPool;
import cc.gps.thread.ThreadPool;
import cc.gps.util.Cache;
import cc.gps.util.SerializeUtil;
import cc.util.db.DataSourceUtil;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Global {
	private static final Log log = LogFactory.getLog(Global.class);
	
	//线程池
	public static ExecutorService pool = Executors.newCachedThreadPool();
	
	//转发队列
	public static GPSQueue<JTReceiveData> transData =null;
	//日志队列
	public static  GPSQueue<String> logData =null;
	//终端－－接收内容　
	public static HashMap<String,String> CID2Body=new HashMap<String,String>();
	
	//终端－－接收内容　(卡号 ,分包值)
	public static HashMap<String,LZBusImage> CID2BodyArray=new HashMap<String,LZBusImage>(); //lzbus lztaxi专用
	public static HashMap<String,JTDataAssemble> JTCID2BodyArray=new HashMap<String,JTDataAssemble>(); //jt专用
	
	//终端－－当前不完整切片　
	public static HashMap<String,Integer> CID2IMGNum=new HashMap<String,Integer>();
	
	//终端－－接收内容转换为类　
	public static HashMap<String,JTReceiveData> CID2BodyClass=new HashMap<String,JTReceiveData>(); 
	//终端－－待接收报文的数量　
	public static HashMap<String,Integer> CID2NUM=new HashMap<String,Integer>();
	
	//终端－－待接收报文是否完整　0完整  其他值为差的数据个数  1 不完整
	public static HashMap<String,Integer> CID2END=new HashMap<String,Integer>();
	//终端－－上一位置
	public static HashMap<String,JT0x0200> CID2POS=new HashMap<String,JT0x0200>();	
	//终端---上次漂移后累计里程
	public static HashMap<String,Double> CID2Miles=new HashMap<String,Double>();
	
	//缓存司机编号
	public static HashMap<String,String> CID2Driver=new HashMap<String,String>();
	//缓存线路编号
	public static HashMap<String,String> CID2Line=new HashMap<String,String>();
	//缓存是否下线
	public static HashMap<String,Integer> CID2ONOFF=new HashMap<String,Integer>();  //1 在线  0 下线
	//sim卡号---终端自编号
	//public static HashMap<String,String> VBHBySIM=new HashMap<String,String>();  //<sim,vbh>
	

	//写数据库线程+处理报文线程  池
	public static final ThreadPool tp = ThreadPool.getInstance(Integer.parseInt(Config.getKey("ThreadPoolSize")));
	
	//key 在channel中存储clientid,用于存储终端ID
	public final static AttributeKey<String> CLIENTID =new AttributeKey<String>("CLIENTID");
	//key 在channel中存储点名次数
	public final static AttributeKey<Integer> CALLCOUNT =new AttributeKey<Integer>("CALLCOUNT");
	//key 在channel中存储MOINTORID
	public final static AttributeKey<String> MONITORID =new AttributeKey<String>("MONITORID");
	//key 在channel中存储MOINTORID
	public final static AttributeKey<String> WEBID =new AttributeKey<String>("WEBID");
	// 缓存已下发指令，等待检查终端是否应答,由cc.gps.thread.CheckSendOrderThread负责检查
	public   static ConcurrentHashMap<String,X2CData> orderSended=new ConcurrentHashMap<String,X2CData>();
	//生成终端流水号
	//private static ConcurrentHashMap<String,Integer> HSID=new ConcurrentHashMap<String,Integer>();
	public final static HashSet DriverSet = new HashSet();
	public static  FileChannel FC = null;
	
	public static HashMap<String,String> LINENV=new HashMap<String,String>();
	
	//缓存原始报文的线程池
	public static SaveGPSPacketPool saveGpsPacketPool =SaveGPSPacketPool.getInstance();
	//缓存解析后报文的线程池
	public static Save2DBTaskPool save2DBTaskPool =Save2DBTaskPool.getInstance();
	//缓存报警信息的线程池
	public static SaveWarnTaskPool saveWarnTaskPool=SaveWarnTaskPool.getInstance();
	//缓存待向平台转发对象的线程池
	public static SaveObjTaskPool saveObjTaskPool=SaveObjTaskPool.getInstance();
	public static void initAll(){
		//initVBHBySIM();
		initLogData();
		//重新初始化司机卡号定时器
		initReInit();
		//初始化司机卡号
		//initDriverNO(); 被告上面的替换
		//initTransQueue();
		initWriteFrontLOG();
		
		//初始化发往平台数据队列的观察者
		//initWebData();
		//初始化累计里程
		//initMiles();
		//初始化下发线程
		FetchOrderThread fot=new FetchOrderThread();
		fot.start();
		//初始化检查下发指令线程
		CheckSendOrderThread cst=new CheckSendOrderThread();
		cst.start();
	}
	/*
	private static void initMiles(){
		TableCtrl tc = new TableCtrl();
		String sql="SELECT telephone,distanceDiff FROM GpsReceRealTime2";
		Connection con=tc.getConn();
		try {
			PreparedStatement pst=con.prepareStatement(sql);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				String sim="0"+rs.getString("telephone").substring(2);
				Double distance=rs.getDouble("distanceDiff");
				Global.CID2Miles.put(sim,distance);
				//log.info(sim+":"+distance);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			tc.freeConn();
		}
		
	}*/
	/*
	private static void initVBHBySIM(){
		TableCtrl tc = new TableCtrl();
		String sql="SELECT simnum,vbh FROM VEHICLE";
		Vector<Hashtable> v=tc.open(sql, 0);
		for(int i=0;i<v.size();i++){
			Hashtable h=v.get(i);
			VBHBySIM.put("0"+((String)h.get("simnum")).substring(2), (String)h.get("vbh"));
		}
		
	}*/
	private static void initLogData(){
		if(logData==null){
			logData = new GPSQueue(Integer.parseInt(Config.getKey("logDataCacheSize")));
		}
		logData.addObserver(new GPSLogObserver());
	}
	/*
	private static void initTransQueue(){
		if(transData==null){
			transData = new GPSQueue(Integer.parseInt(Config.getKey("transQueueCacheSize")));
		}
	}*/
	private static  void initWriteFrontLOG() {  //保存前端日志文件
		Timer timer  = new Timer(true);
        timer.schedule(new CreatLOGFile(),0,1000*60*60*24);  //定时重新创建log文件
		/*
		if(FC==null){
			RandomAccessFile file=null;
			try {
				file = new RandomAccessFile (Config.getKey("GPSLOG"), "rw");
				FC = file.getChannel();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}
	
	//重新初始化司机卡号
	private static  void initReInit() {  //重新初始化司机卡号
		Timer timer  = new Timer(true);
        timer.schedule(new ReInit(),0,1000*60*60);  //1小时检测1次   毫 秒*60*60
	}

	//保存待转发到Web的信息  队列
	
	public static GPSQueue<JTReceiveData> webData =null;
	private static void initWebData(){
		log.debug("初始化Web转发观察者");
		if(webData==null){
			webData = new GPSQueue<JTReceiveData>(Integer.parseInt(Config.getKey("webDataCacheSize")));
		}
		webData.addObserver(new GPSPosObserver());
	}
	//预加载所有司机卡号
	public static void initDriverNO(){
		Connection con=DataSourceUtil.getConn();
		String sql="select di_iccardno from driver_information where di_iccardno is not null and di_iccardno<>''";
		try {
			PreparedStatement pst=con.prepareStatement(sql);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				DriverSet.add(rs.getString(1));
			}
			rs.close();
			pst.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	/*
	public synchronized static  int creatSID(String clientID){
		Integer sid = HSID.get(clientID);
		if(sid==null){
			sid=1;
			HSID.put(clientID, 1);
		}else{
			if(sid>65530) sid=0;
			HSID.put(clientID, ++sid);
			//System.out.println("生成的serialID;"+sid);
		}
		
		return sid;
	}*/
	
	public static String getLastDriverNo(String telephone){
		Connection con=DataSourceUtil.getConn();
		//String sql="select top 1 driverno from driverlog where login=1 and telephone=?  order by gpstime desc";
		String sql="select top 1 driverno from driverlog where login=1 and telephone=? and gpstime>convert(varchar(10),getdate(),120) order by gpstime desc";
		String driverNo=null;
		try {
			PreparedStatement pst=con.prepareStatement(sql);
			pst.setString(1, telephone);//8615320424084
			ResultSet rs=pst.executeQuery();
			if(rs.next()){
				driverNo=rs.getString(1);
			}
			rs.close();
			pst.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//log.debug(telephone+"********"+driverNo);
		return driverNo;
	}
	public static String getLineVersion(String lineNO){
		Connection con=DataSourceUtil.getConn();
		String sql="SELECT version FROM LINE_BASEINFO WHERE LINE_CODE=?";
		String lineVersion="";
		try {
			PreparedStatement pst=con.prepareStatement(sql);
			pst.setString(1, lineNO);
			ResultSet rs=pst.executeQuery();
			if(rs.next()){
				lineVersion=rs.getString(1);
			}
			rs.close();
			pst.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		//log.debug(telephone+"********"+driverNo);
		return lineVersion;
	}
	//记录报文日志
	public static int add(String ctype,String rs,String messageID,String clientID,String body){
		Connection con=DataSourceUtil.getConn();
		String sql="insert into GPSPacketLOG(id,ctype,rtime,rs,messageID,sim,body) values(?,?,?,?,?,?,?)";
		String uid=UUID.randomUUID().toString();
		int n=0;
		PreparedStatement pst=null;
		try {
			pst=con.prepareStatement(sql);
			pst.setString(1, uid);
			pst.setString(2, ctype);
			pst.setString(3,new Date().toLocaleString());
			pst.setString(4, rs);
			pst.setString(5, messageID);
			pst.setString(6, clientID);
			pst.setString(7, body);
			//pst.setBytes(8,SerializeUtil.serializeObject(obj));
			n=pst.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pst!=null)
				try {
					pst.close();
					 con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		return n;
	}
}
