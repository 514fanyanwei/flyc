package cc.gps.util;

public class Point {    
	/** 经度 */   
	private double longitude; 
	/** 纬度  */         
	private double latitude;     
	
	public Point(){}
	public Point(double longitude,double latitude){
		this.longitude=longitude;
		this.latitude=latitude;
	}
	public void setLongitude(double longitude) {       
		this.longitude = longitude;      
	}          
	public void setLatitude(double latitude) {     
		this.latitude = latitude;
	}
	 
	public double getLongitude() {             
		return longitude;        
	}         
	public double getLatitude() {      
		return latitude;      
	}  
}