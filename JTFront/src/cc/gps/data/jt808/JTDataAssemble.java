package cc.gps.data.jt808;

import java.util.HashSet;
import java.util.Set;

public class JTDataAssemble {
	public int total;//切片个数
	public Set<Integer> unfinished=new HashSet<Integer>(); //未收到的分组序号
	public String image[];
	
	public JTDataAssemble(int total){
		this.total=total;
		image=new String[total];
		for(int i=0;i<total;i++){
			unfinished.add(i+1);
		}
	}
}
