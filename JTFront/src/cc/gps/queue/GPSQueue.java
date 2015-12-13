package cc.gps.queue;

import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cc.gps.config.Config;


public class GPSQueue<T> extends Observable{
	private int size;
    
	public BlockingQueue<T> queueData;
    
			
    public  GPSQueue(int size){
    	this.size = size;
    	queueData=new LinkedBlockingQueue<T>(size);  //大小
    }
    
    /*
    public GPSQueue(int size,long time){
    	this.size = size;
    	queueData=new LinkedBlockingQueue<T>(size);  //大小
    }*/
    
    public synchronized void add(T obj){
    	
    	queueData.offer(obj);
    	
    	if(queueData.size()>size*3/4){
    		setChanged();
    		notifyObservers(this);
    	}
    }
    public synchronized void add(T obj,boolean f){  //f为true立即触发
    	if((f==false)){
    		add(obj);
    	}else{
    		//log.info("立即触发");
    		queueData.offer(obj);
    		setChanged();
    		notifyObservers(this);
    	}
    }
    //主动触发
    public synchronized void trigger(){
    	setChanged();
    	notifyObservers(this);
    }
    
    public  int size(){
    	return queueData.size();
    }
    
    public synchronized T remove(){
    	return queueData.poll();
    }
}
