package cc.web.pageModel;

import java.io.Serializable;

public class PGPS implements Serializable{
	String type;//查询方式
	String body;//查询内容
	String psw;//用户密码
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
	
	
	
	

}
