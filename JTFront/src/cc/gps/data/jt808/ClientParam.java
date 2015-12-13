package cc.gps.data.jt808;

public class ClientParam {
	int  heartbeat;  //0x0001
	int  tcpReplyTimeOut; 
	int  tcpRepeatSend;
	int  udpReplyTimeOut;
	int  udpRepeatSend;
	int  smsReplyTimeOut;
	int  smsRepeatSend;
	
	String mapnORppp;
	String muserName;
	String mpsw;
	String mAddress;
	
	String sapnORppp;
	String suserName;
	String spsw;
	String sAddress;
	
	int tcpPort;
	int udpPort;
	
	int wzhbcl;
	int wzhbfa;
	
	int wdlTimeInterval; //0x0022
	
	int xmTimeInterval;
	int bjTimeInterval;
	
	int timeInterval; //0x0029
	
	int distanceInterval;//0x002C
	
	int wdlDistanceInterval;  //0x002D
	int xmDistanceInterval;//0x002e
	int bjDistanceInterval; //0x002f
	int gdbc;//0x0030
	
	String mphone; //0x0040
	String fphone; //0x0041
	String hphone; //0x0042
	String sphone; //0x0043
	String jphone; //0x0044
	
	int dhjtcl;//0x0045
	int holdTime; //0x0046
	int mTime; //0x0047
	String jtphone;// 0x0048
	String tqdxphone; //0x0049
	
	int warnShield;//0x0050
	int warnSwitch;//0x0051
	int warnShotSwitch;//0x0052
	int warnShotStoreFalg;//0x0053
	int keyFlag ;//0x0054
	
	int maxVelocity;//最高速度 0x0054
	int overSpeedTime; //0x0056 
	
	int continuousDriving;  //0x0057
	int dayContinuousDriving; //0x0058
	
	int minRest; //0x0059
	int maxStop;  //0x0060
	
	int videoQuality; //0x0070
	int lighteness;//0x0071
	int contrast;//0x0072
	int saturation;//0x0073
	int chroma;//0x0074
	
	int mileage;//0x0080
	int provinceID;//0x0081
	int cityID;//0x0082
	String carNum; //0x0083
	int color;   //0x0084
	public int getHeartbeat() {
		return heartbeat;
	}
	public void setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
	}
	public int getTcpReplyTimeOut() {
		return tcpReplyTimeOut;
	}
	public void setTcpReplyTimeOut(int tcpReplyTimeOut) {
		this.tcpReplyTimeOut = tcpReplyTimeOut;
	}
	public int getTcpRepeatSend() {
		return tcpRepeatSend;
	}
	public void setTcpRepeatSend(int tcpRepeatSend) {
		this.tcpRepeatSend = tcpRepeatSend;
	}
	public int getUdpReplyTimeOut() {
		return udpReplyTimeOut;
	}
	public void setUdpReplyTimeOut(int udpReplyTimeOut) {
		this.udpReplyTimeOut = udpReplyTimeOut;
	}
	public int getUdpRepeatSend() {
		return udpRepeatSend;
	}
	public void setUdpRepeatSend(int udpRepeatSend) {
		this.udpRepeatSend = udpRepeatSend;
	}
	public int getSmsReplyTimeOut() {
		return smsReplyTimeOut;
	}
	public void setSmsReplyTimeOut(int smsReplyTimeOut) {
		this.smsReplyTimeOut = smsReplyTimeOut;
	}
	public int getSmsRepeatSend() {
		return smsRepeatSend;
	}
	public void setSmsRepeatSend(int smsRepeatSend) {
		this.smsRepeatSend = smsRepeatSend;
	}
	public String getMapnORppp() {
		return mapnORppp;
	}
	public void setMapnORppp(String mapnORppp) {
		this.mapnORppp = mapnORppp;
	}
	public String getMuserName() {
		return muserName;
	}
	public void setMuserName(String muserName) {
		this.muserName = muserName;
	}
	public String getMpsw() {
		return mpsw;
	}
	public void setMpsw(String mpsw) {
		this.mpsw = mpsw;
	}
	public String getmAddress() {
		return mAddress;
	}
	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}
	public String getSapnORppp() {
		return sapnORppp;
	}
	public void setSapnORppp(String sapnORppp) {
		this.sapnORppp = sapnORppp;
	}
	public String getSuserName() {
		return suserName;
	}
	public void setSuserName(String suserName) {
		this.suserName = suserName;
	}
	public String getSpsw() {
		return spsw;
	}
	public void setSpsw(String spsw) {
		this.spsw = spsw;
	}
	public String getsAddress() {
		return sAddress;
	}
	public void setsAddress(String sAddress) {
		this.sAddress = sAddress;
	}
	public int getTcpPort() {
		return tcpPort;
	}
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}
	public int getUdpPort() {
		return udpPort;
	}
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
	public int getWzhbcl() {
		return wzhbcl;
	}
	public void setWzhbcl(int wzhbcl) {
		this.wzhbcl = wzhbcl;
	}
	public int getWzhbfa() {
		return wzhbfa;
	}
	public void setWzhbfa(int wzhbfa) {
		this.wzhbfa = wzhbfa;
	}
	public int getWdlTimeInterval() {
		return wdlTimeInterval;
	}
	public void setWdlTimeInterval(int wdlTimeInterval) {
		this.wdlTimeInterval = wdlTimeInterval;
	}
	public int getXmTimeInterval() {
		return xmTimeInterval;
	}
	public void setXmTimeInterval(int xmTimeInterval) {
		this.xmTimeInterval = xmTimeInterval;
	}
	public int getBjTimeInterval() {
		return bjTimeInterval;
	}
	public void setBjTimeInterval(int bjTimeInterval) {
		this.bjTimeInterval = bjTimeInterval;
	}
	public int getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}
	public int getDistanceInterval() {
		return distanceInterval;
	}
	public void setDistanceInterval(int distanceInterval) {
		this.distanceInterval = distanceInterval;
	}
	public int getWdlDistanceInterval() {
		return wdlDistanceInterval;
	}
	public void setWdlDistanceInterval(int wdlDistanceInterval) {
		this.wdlDistanceInterval = wdlDistanceInterval;
	}
	public int getXmDistanceInterval() {
		return xmDistanceInterval;
	}
	public void setXmDistanceInterval(int xmDistanceInterval) {
		this.xmDistanceInterval = xmDistanceInterval;
	}
	public int getBjDistanceInterval() {
		return bjDistanceInterval;
	}
	public void setBjDistanceInterval(int bjDistanceInterval) {
		this.bjDistanceInterval = bjDistanceInterval;
	}
	public int getGdbc() {
		return gdbc;
	}
	public void setGdbc(int gdbc) {
		this.gdbc = gdbc;
	}
	public String getMphone() {
		return mphone;
	}
	public void setMphone(String mphone) {
		this.mphone = mphone;
	}
	public String getFphone() {
		return fphone;
	}
	public void setFphone(String fphone) {
		this.fphone = fphone;
	}
	public String getHphone() {
		return hphone;
	}
	public void setHphone(String hphone) {
		this.hphone = hphone;
	}
	public String getSphone() {
		return sphone;
	}
	public void setSphone(String sphone) {
		this.sphone = sphone;
	}
	public String getJphone() {
		return jphone;
	}
	public void setJphone(String jphone) {
		this.jphone = jphone;
	}
	public int getDhjtcl() {
		return dhjtcl;
	}
	public void setDhjtcl(int dhjtcl) {
		this.dhjtcl = dhjtcl;
	}
	public int getHoldTime() {
		return holdTime;
	}
	public void setHoldTime(int holdTime) {
		this.holdTime = holdTime;
	}
	public int getmTime() {
		return mTime;
	}
	public void setmTime(int mTime) {
		this.mTime = mTime;
	}
	public String getJtphone() {
		return jtphone;
	}
	public void setJtphone(String jtphone) {
		this.jtphone = jtphone;
	}
	public String getTqdxphone() {
		return tqdxphone;
	}
	public void setTqdxphone(String tqdxphone) {
		this.tqdxphone = tqdxphone;
	}
	public int getWarnShield() {
		return warnShield;
	}
	public void setWarnShield(int warnShield) {
		this.warnShield = warnShield;
	}
	public int getWarnSwitch() {
		return warnSwitch;
	}
	public void setWarnSwitch(int warnSwitch) {
		this.warnSwitch = warnSwitch;
	}
	public int getWarnShotSwitch() {
		return warnShotSwitch;
	}
	public void setWarnShotSwitch(int warnShotSwitch) {
		this.warnShotSwitch = warnShotSwitch;
	}
	public int getWarnShotStoreFalg() {
		return warnShotStoreFalg;
	}
	public void setWarnShotStoreFalg(int warnShotStoreFalg) {
		this.warnShotStoreFalg = warnShotStoreFalg;
	}
	public int getKeyFlag() {
		return keyFlag;
	}
	public void setKeyFlag(int keyFlag) {
		this.keyFlag = keyFlag;
	}
	public int getMaxVelocity() {
		return maxVelocity;
	}
	public void setMaxVelocity(int maxVelocity) {
		this.maxVelocity = maxVelocity;
	}
	public int getOverSpeedTime() {
		return overSpeedTime;
	}
	public void setOverSpeedTime(int overSpeedTime) {
		this.overSpeedTime = overSpeedTime;
	}
	public int getContinuousDriving() {
		return continuousDriving;
	}
	public void setContinuousDriving(int continuousDriving) {
		this.continuousDriving = continuousDriving;
	}
	public int getDayContinuousDriving() {
		return dayContinuousDriving;
	}
	public void setDayContinuousDriving(int dayContinuousDriving) {
		this.dayContinuousDriving = dayContinuousDriving;
	}
	public int getMinRest() {
		return minRest;
	}
	public void setMinRest(int minRest) {
		this.minRest = minRest;
	}
	public int getMaxStop() {
		return maxStop;
	}
	public void setMaxStop(int maxStop) {
		this.maxStop = maxStop;
	}
	public int getVideoQuality() {
		return videoQuality;
	}
	public void setVideoQuality(int videoQuality) {
		this.videoQuality = videoQuality;
	}
	public int getLighteness() {
		return lighteness;
	}
	public void setLighteness(int lighteness) {
		this.lighteness = lighteness;
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
	public int getMileage() {
		return mileage;
	}
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	public int getProvinceID() {
		return provinceID;
	}
	public void setProvinceID(int provinceID) {
		this.provinceID = provinceID;
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
	
	
}
