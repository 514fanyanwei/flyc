package cc.gps.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.jt808.JT0x0200;
import cc.gps.data.jt808.JTReceiveData;
import cc.gps.data.jt808.VWarns;
import cc.util.db.DataSourceUtil;

public class SaveWarnTaskPool {
	private static final Log log = LogFactory.getLog(SaveWarnTaskPool.class);
	/* 单例 */
	private static SaveWarnTaskPool instance ;//= ThreadPool.getInstance();
	
	public static final int SYSTEM_BUSY_TASK_COUNT = 150;
	/* 默认池中线程数 */
	public static int worker_num = 5;  //
	/* 已经处理的任务数 */
	private static long taskCounter = 0;
	//第次最多批处理终端报文数(对于报警信息，1个报文可能有多条记录,因为可能有多种报警)
	private static int batch_num=100;
	
	public static boolean systemIsBusy = false;
	
	private static BlockingQueue<JT0x0200> queue = new LinkedBlockingQueue();
	/* 池中的所有线程 */
	public PoolWorker[] workers;
	
	private SaveWarnTaskPool() {
	    workers = new PoolWorker[worker_num];
	    for (int i = 0; i < workers.length; i++) {
	        workers[i] = new PoolWorker(i);
	    }
	}
	
	private SaveWarnTaskPool(int pool_worker_num) {
		log.debug("初始化存储原始报文处理线程池,大小:"+pool_worker_num);
	    worker_num = pool_worker_num;
	    workers = new PoolWorker[worker_num];
	    for (int i = 0; i < workers.length; i++) {
	        workers[i] = new PoolWorker(i);
	    }
	}
	
	public static synchronized SaveWarnTaskPool getInstance() {
		if (instance == null){
			//worker_num = pool_worker_num;
	        return new SaveWarnTaskPool(worker_num);
		}
	    return instance;
	}
	/**
	* 增加新的任务
	* 每增加一个新任务，都要唤醒任务队列
	* @param newTask
	*/
	public void add(JTReceiveData gps) {
		if (gps instanceof JT0x0200){
			log.debug("add new gps to save warn task");
			if(!queue.offer((JT0x0200)gps)){
				log.debug(" saveWarnQueue is full");
				
			}
			/* 唤醒队列, 开始执行 */
	       // queue.notifyAll();
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
		Connection con2;
		String sql1="insert into gpswarn(wid,simnum,gpstime,warnType,recetime) values(?,?,?,?,?)";
		String sql2="insert into gpswarn2(wid,simnum,gpstime,warnType,recetime) values(?,?,?,?,?)";
		
		PreparedStatement pst1=null;
    	PreparedStatement pst2=null;
    	
	    private int index = -1;
	    /* 该工作线程是否有效 */
	    private boolean isRunning = true;
	    /* 用待执行的处理 */
	    private boolean isWaiting = false;
	
	    public PoolWorker(int index) {
	        this.index = index;
	        try{
	    		con1=DataSourceUtil.getConn();
	    		con2=DataSourceUtil.getConn();
	    		pst1=con1.prepareStatement(sql1);
		    	pst2=con2.prepareStatement(sql2);
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
	        	JT0x0200 p=null;
	        	try {
					p=queue.take();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
                
                StringBuffer info=new StringBuffer();
            	for(int j=0;j<32;j++){
            		if((p.iwarning&VWarns.WS[j])!=0){
            			info.append(VWarns.WSINFO[j]+"|");
            		}
            	}
        		if(info.length()==0) continue; //处理下一个报文
        		String winfo[]=info.toString().split("\\|");
    			//存储报警记录
    			//String sql1="insert into gpswarn(wid,simnum,gpstime,warnType,recetime) values(?,?,?,?,?)";
    			try {  
        			String sim="86"+p.head.phone.substring(1);
        			String current=new Date().toLocaleString();
    				pst1.setString(2, sim);
        			pst1.setString(3, p.time);
        			pst1.setString(5, current);
        			
        			//更新实时报警表   gpswarn2
        			pst2.setString(2, sim);
        			pst2.setString(3, p.time);
        			pst2.setString(5, current);
           		    			
        			for(int k=0;k<winfo.length;k++){
        				num++;//计数
        				String uid=UUID.randomUUID().toString();
        				pst1.setString(1, uid);
        				pst1.setString(4, winfo[k]);
        				pst1.addBatch();
        				
        				pst2.setString(1, uid);
        				pst2.setString(4, winfo[k]);
        				pst2.addBatch();
        			}
        			
        		}catch(Exception e){
        			e.printStackTrace();
        		}
    			if((num>=batch_num)||queue.isEmpty()){
    				try{
    	            	pst1.executeBatch();
    	                pst2.executeBatch();
    	                log.debug("save warn thread "+index+" ********"+num);
    	                //重新初始化
    	                num=0;
    	                /*
    	                pst1.close();
    	                pst2.close();
    	                //con1.close();
    	                //con2.close();
    	                
	    	    		con1=DataSourceUtil.getConn();
	    	    		con2=DataSourceUtil.getConn();
	    	    		pst1=con1.prepareStatement(sql1);
	    		    	pst2=con2.prepareStatement(sql2);*/
    	                
    	            }catch(Exception e){
    	            	e.printStackTrace();
    	            }
    			}
            
                
	        }
	    }
	}
	
	public static void main(String args[])
	{
		ThreadPool tp = ThreadPool.getInstance(10);
		for(int i=0;i<5;i++){
		//Task t1 = new Save2DBTask("select * from role");
		//tp.addTask(t1);
		}
		
	}
}