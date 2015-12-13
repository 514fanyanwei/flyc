package cc.gps.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VWarns;
import cc.gps.util.Ecode;
import cc.gps.util.SerializeUtil;
import cc.util.db.DataSourceUtil;

public class SaveObjTaskPool {
	private static final Log log = LogFactory.getLog(SaveObjTaskPool.class);
	/* 单例 */
	private static SaveObjTaskPool instance ;//= ThreadPool.getInstance();
	
	public static final int SYSTEM_BUSY_TASK_COUNT = 150;
	/* 默认池中线程数 */
	public static int worker_num = 5;  //
	/* 已经处理的任务数 */
	private static long taskCounter = 0;
	//第次最多批处理终端报文数(对于报警信息，1个报文可能有多条记录,因为可能有多种报警)
	private static int batch_num=100;
	
	public static boolean systemIsBusy = false;
	
	private static BlockingQueue<JTReceiveData> queue = new LinkedBlockingQueue();
	/* 池中的所有线程 */
	public PoolWorker[] workers;
	
	private SaveObjTaskPool() {
	    workers = new PoolWorker[worker_num];
	    for (int i = 0; i < workers.length; i++) {
	        workers[i] = new PoolWorker(i);
	    }
	}
	
	private SaveObjTaskPool(int pool_worker_num) {
		log.debug("初始化向平台转发报文处理线程池,大小:"+pool_worker_num);
	    worker_num = pool_worker_num;
	    workers = new PoolWorker[worker_num];
	    for (int i = 0; i < workers.length; i++) {
	        workers[i] = new PoolWorker(i);
	    }
	}
	
	public static synchronized SaveObjTaskPool getInstance() {
		if (instance == null){
			//worker_num = pool_worker_num;
	        return new SaveObjTaskPool(worker_num);
		}
	    return instance;
	}
	/**
	* 增加新的任务
	* 每增加一个新任务，都要唤醒任务队列
	* @param newTask
	*/
	public void add(JTReceiveData gps) {
		log.debug("加入从终端收到的待转发的报文,messageID:0X"+Ecode.DEC2HEX(gps.head.messageID));
		if(!queue.offer((JTReceiveData)gps)){
			log.warn(" 待转发队列满");
		}
	}
	

	/**
	* 销毁线程池
	*/
	public synchronized void destroy() {
	    for (int i = 0; i < worker_num; i++) {
	        workers[i].stopWorker();
	        workers[i] = null;
	    }
	    queue.clear();
	}
	
	
	/**
	* 池中工作线程
	* 
	* @author fanhl
	*/
	private class PoolWorker extends Thread {
		Connection con1;
		
		String insertSQL="insert into rgps(rid,saveTime,messageID,serialID,clientID,obj) values(?,?,?,?,?,?)";
		PreparedStatement pst1=null;
    	
	    private int index = -1;
	    /* 该工作线程是否有效 */
	    private boolean isRunning = true;
	    /* 用待执行的处理 */
	    private boolean isWaiting = false;
	
	    public PoolWorker(int index) {
	        this.index = index;
	        try{
	    		con1=DataSourceUtil.getConn();
	    		pst1=con1.prepareStatement(insertSQL);
		    	
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	        start();
	    }
	
	    public void stopWorker() {
	        this.isRunning = false;
	    }
	
	    public boolean isWaiting() {
	        return this.isWaiting;
	    }
	    /**
	    * 循环执行任务
	    * 线程池的关键所在
	    */
	    public void run() {
	    	int num=0;//统计待执行sql个数
	        while (isRunning) {
	        	 /* 取出任务执行 */
	        	JTReceiveData p=null;
	        	try {
					p=queue.take();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
                //存储待向平台转发报文对象
	        	try{
	    			num++;
	    			String uid=UUID.randomUUID().toString();
	        		pst1.setString(1, uid);
	    			pst1.setString(2, new Date().toLocaleString());
	    			pst1.setInt(3, p.head.messageID);
	    			pst1.setLong(4,p.head.serialID);
	    			pst1.setString(5,p.head.phone);
	    			pst1.setBytes(6, SerializeUtil.serializeObject(p));
	    			pst1.addBatch();
	    		}catch(Exception e){e.printStackTrace();}
    			if((num>=batch_num)||queue.isEmpty()){
    				try{
    	            	pst1.executeBatch();
    	                //log.debug("save obj thread "+index+" ********"+num);
    	                //重新初始化
    	                num=0;
    	                
    	            }catch(Exception e){
    	            	e.printStackTrace();
    	            }
    			}
            
                
	        }
	    }
	}
}