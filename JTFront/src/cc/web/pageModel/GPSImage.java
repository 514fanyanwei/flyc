package cc.web.pageModel;

import java.io.Serializable;
import java.util.List;

public class GPSImage implements Serializable{
	String type;//查询方式
	String body;//查询内容
	String psw;//用户密码
	
	List cameraID;  //摄像头选择
	int cameraCount;//拍照次数
	
	int channelID; //通道ID
	int cameraOrder;  //拍摄命令
	int interval;  //拍照间隔
	int hold; //保持标志
	int resolution;//分辨率
	int quality; //图像质量
	
	int brightness;//亮度
	int contrast;//对比度
	int saturation;//饱和度
	int chroma; //色度
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getPsw() {
		return psw;
	}
	public void setPsw(String psw) {
		this.psw = psw;
	}
	public int getChannelID() {
		return channelID;
	}
	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}
	public int getCameraOrder() {
		return cameraOrder;
	}
	public void setCameraOrder(int cameraOrder) {
		this.cameraOrder = cameraOrder;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getHold() {
		return hold;
	}
	public void setHold(int hold) {
		this.hold = hold;
	}
	public int getResolution() {
		return resolution;
	}
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	public int getBrightness() {
		return brightness;
	}
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}
	public int getContrast() {
		return contrast;
	}
	public void setContrast(int contrast) {
		this.contrast = contrast;
	}
	public int getSaturation() {
		return saturation;
	}
	public void setSaturation(int saturation) {
		this.saturation = saturation;
	}
	public int getChroma() {
		return chroma;
	}
	public void setChroma(int chroma) {
		this.chroma = chroma;
	}
	public List getCameraID() {
		return cameraID;
	}
	public void setCameraID(List cameraID) {
		this.cameraID = cameraID;
	}
	public int getCameraCount() {
		return cameraCount;
	}
	public void setCameraCount(int cameraCount) {
		this.cameraCount = cameraCount;
	}
	
	

}
