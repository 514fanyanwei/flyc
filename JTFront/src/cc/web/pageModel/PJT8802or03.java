package cc.web.pageModel;

import java.io.Serializable;

public class PJT8802or03  extends PGPS implements Serializable{
	int mtype;
	int channelID;
	int eventID;
	String startTime;
	String endTime;
	int delID=-1;  //-1 默认为检索
	
	public int getMtype() {
		return mtype;
	}
	public void setMtype(int mtype) {
		this.mtype = mtype;
	}
	public int getChannelID() {
		return channelID;
	}
	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}
	public int getEventID() {
		return eventID;
	}
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getDelID() {
		return delID;
	}
	public void setDelID(int delID) {
		this.delID = delID;
	}
	
	

}
