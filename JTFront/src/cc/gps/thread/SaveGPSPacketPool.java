package cc.gps.thread;
/*保存终端上传的原始报文*/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.util.db.DataSourceUtil;


public class SaveGPSPacketPool {
	private static final Log logger = LogFactory.getLog(SaveGPSPacketPool.class);
	/* 单例 */
	private static SaveGPSPacketPool instance ;//= ThreadPool.getInstance();
	
	public static final int SYSTEM_BUSY_TASK_COUNT = 150;
	/* 默认池中线程数 */
	public static int worker_num = 10;  //
	/* 已经处理的任务数 */
	private static long taskCounter = 0;
	//第次最多批处理终端报文数(对于报警信息，1个报文可能有多条记录,因为可能有多种报警)
	private static int batch_num=100;
	public static boolean systemIsBusy = false;
	
	private static List<Packet> taskQueue = Collections.synchronizedList(new LinkedList<Packet>());
	/* 池中的所有线程 */
	public PoolWorker[] workers;
	
	private SaveGPSPacketPool() {
	    workers = new PoolWorker[worker_num];
	    for (int i = 0; i < workers.length; i++) {
	        workers[i] = new PoolWorker(i);
	    }
	}
	
	private SaveGPSPacketPool(int pool_worker_num) {
		logger.debug("初始化存储原始报文处理线程池,大小:"+pool_worker_num);
	    worker_num = pool_worker_num;
	    workers = new PoolWorker[worker_num];
	    for (int i = 0; i < workers.length; i++) {
	        workers[i] = new PoolWorker(i);
	    }
	}
	
	public static synchronized SaveGPSPacketPool getInstance() {
		if (instance == null){
			//worker_num = pool_worker_num;
	        return new SaveGPSPacketPool(worker_num);
		}
	    return instance;
	}
	/**
	* 增加新的任务
	* 每增加一个新任务，都要唤醒任务队列
	* @param newTask
	*/
	public void add(Packet packet) {
	    synchronized (taskQueue) {
	    	/*
	    	if(taskCounter>Long.MAX_VALUE-10){
	    			taskCounter=0;
	    	}*/
	        taskQueue.add(packet);
	        /* 唤醒队列, 开始执行 */
	        taskQueue.notifyAll();
	    }
	    //logger.debug("Submit Task<" + newTask.getTaskId() + ">: "   + newTask.info());
	}
	
	/**
	* 线程池信息
	* @return
	*/
	public String getInfo() {
	    StringBuffer sb = new StringBuffer();
	    sb.append("\nTask Queue Size:" + taskQueue.size());
	    for (int i = 0; i < workers.length; i++) {
	        sb.append("\nWorker " + i + " is "
	                + ((workers[i].isWaiting()) ? "Waiting." : "Running."));
	    }
	    return sb.toString();
	}
	/**
	* 销毁线程池
	*/
	public synchronized void destroy() {
	    for (int i = 0; i < worker_num; i++) {
	        workers[i].stopWorker();
	        workers[i] = null;
	    }
	    taskQueue.clear();
	}
	
	
	/**
	* 池中工作线程
	* 
	* @author fanhl
	*/
	private class PoolWorker extends Thread {
		private String sql="insert into GPSPacketLOG(id,ctype,rtime,rs,messageID,sim,body) values(?,?,?,?,?,?,?)";
		private Connection con=DataSourceUtil.getConn();
	    private int index = -1;
	    
	    /* 该工作线程是否有效 */
	    private boolean isRunning = true;
	    /* 用待执行的处理 */
	    private boolean isWaiting = false;
	
	    public PoolWorker(int index) {
	        this.index = index;
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
	    	PreparedStatement pst=null;
			try {
				pst = con.prepareStatement(sql);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int num=0;
	        while (isRunning) {
	        	Packet p=null;
	            synchronized (taskQueue) {
	                while (taskQueue.isEmpty()) {
	                    try {
	                    	taskQueue.wait();
	                    } catch (Exception ie) {
	                        logger.error(ie);
	                    }
	                }
	                /* 取出任务执行 */
	                p = (Packet) taskQueue.remove(0);
	            }
	            if (p != null) {
	            	num++;
	                isWaiting = true;
	                try {
                		pst.setString(1, UUID.randomUUID().toString());
            			pst.setString(2, p.ctype);
            			pst.setString(3,new Date().toLocaleString());
            			pst.setString(4, p.rs);
            			pst.setString(5, p.messageID);
            			pst.setString(6, p.clientID);
            			pst.setString(7, p.body);
            			pst.addBatch();
            			if((num>=batch_num)||taskQueue.isEmpty()){
            				pst.executeBatch();
            				num=0;//重新置0,开始计数
            			}
	            		
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    logger.error(e);
	                }
	            	
	                p = null;
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