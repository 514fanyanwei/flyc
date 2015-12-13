package cc.gps.parse;

import cc.gps.util.Ecode;
import io.netty.buffer.ByteBuf;

public class Segment<T> {
	public T value;
	private int start;
	private int len;
	private IParse<T> sjparse;
	public Segment(ByteBuf bb,int start,int len,IParse<T> sjparse){
		this.start=start;
		this.len=len;
		this.sjparse=sjparse;
		try {
			value=sjparse.parse(bb,start,len);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Segment(ByteBuf bb,int len,IParse<T> sjparse){
		this.len=len;
		this.sjparse=sjparse;
		try {
			value=sjparse.parse(bb,len);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	public T getValue(){
		return value;
	}
	public void setValue(T value){
		this.value=value;
	}
	
	public T toT(){
		return value;
	}*/
}
