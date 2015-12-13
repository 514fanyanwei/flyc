package cc.gps.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.jt808.JTProcess0x0102;

public class FileUtil {
	private static final Log log = LogFactory.getLog(FileUtil.class);
	public static LinkedList<String> readFile(String fileName){
		LinkedList<String> bs=new LinkedList<String>();
		
		File file=new File(fileName);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String temp=null;
		    StringBuffer sb=new StringBuffer();
		    temp=br.readLine();
		    int i=0;
		    while(temp!=null){
		       	 log.info(temp);
		       	 bs.add(temp);
	        	 //Byte ps[]=new Byte[temp.length()/2];
	        	 //ps=Ecode.HexString2BSArray(temp.trim());
	        	 temp=br.readLine();
	        	 
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bs;

	}

}
