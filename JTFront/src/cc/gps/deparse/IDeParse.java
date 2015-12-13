package cc.gps.deparse;

public interface IDeParse<T> {
	//len 待生成的长度
	public String deparse(T in,int len) throws DeParseException;
}
