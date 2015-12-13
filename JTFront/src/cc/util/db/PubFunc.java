///////////////////////////////////////////////////// 
//	功能说明：		通用函数类	                  // 
//	作者：			fanhl               	      //
//	编写日期：	    2010/08/23--2010/03/27        //	
//////////////////////////////////////////////////// 

package cc.util.db; 

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.util.DateUtil;
  
public class PubFunc
{
	private static final Log log = LogFactory.getLog(PubFunc.class);
	static private String configfile = "Config";
	static private String endline = "------分隔符------";
	
	//字符串转成整型，非整型字符转为0
	public static int parseInt(String s)
	{
		try{
			if (s == null) 
				return 0;
			else
				return Integer.parseInt(s.trim());
		}
		catch(Exception e){return 0;}
		
	}
	//转为字符串对应的boolean "true" or "1"=true;"false" or "0" or other = false
	public static boolean parseBoolean(String s)
	{
		if (s==null) 
			return false;
		if (s.toLowerCase().trim().equals("true") || s.trim().equals("1"))
			return true;
		else
			return false;
	}
	//字符串转成浮点型，非浮点型字符转为0
	public static double parseDouble(Object s)
	{
		try{
			if (s == null) 
				return 0;
			else
				return Double.parseDouble(((String)s).trim());
		}
		catch(Exception e){return 0;}
		
	}
	//整型转为浮点型
	public static double parseDouble(int i)
	{
		return parseDouble(String.valueOf(i));
	}	
	//整型转为浮点型
	public static double parseDouble(long i)
	{
		return parseDouble(String.valueOf(i));
	}	
	//浮点型转为两位小数点的字符串
	public static String doubleToStr(double f)
	{
		return String.valueOf(parseDouble(Math.round(f*100))/100);
	}	
	
	//Session字符串转成整型，非整型字符转为0
	public static int parseInt(Object s)
	{
		return parseInt(s,0);		
	}	
	//Session字符串转成整型，非整型字符转为默认值
	public static int parseInt(Object s, int def)
	{
		try{
			if (s == null) 
				return def;
			else
				return Integer.parseInt(s.toString());//Integer.parseInt(((String)s).trim());
		}
		catch(Exception e){return def;}
		
	}	
	//Session字符串转成字符串
	public static String toString(Object s)
	{
		try{
			if (s == null) 
				return "";
			else
				return (String)s;
		}
		catch(Exception e){return "";}
		
	}		
	//分隔字符串 参数说明:source为要分隔的串，sign为分隔符
	public static String[] split(String source, char[] sign)
	{
		String[] ret = new String[0];
		if ((source == null) || (source.equals("")))
			return ret;
 		Vector ar = new Vector();
 		int start=0;
 		for (int i=0; i<source.length(); i++)
 		{
 			for (int j=0; j<sign.length; j++)
 			{
 				if (sign[j]==source.charAt(i))
 				{
 					if (start!=i)
 					{
	 					String s=source.substring(start,i); 					
 						ar.add(s);
 					}
 					start = i+1;
 					break;
 				}
 			}
 		}
		if (start<source.length())
			ar.add(source.substring(start,source.length()));
 		ret = new String[ar.size()];
 		for (int i=0; i<ar.size(); i++)
 		{
 			ret[i] = (String)ar.elementAt(i);
 		}
		return ret;	
		
	}
	//分隔字符串 参数说明:source为要分隔的串，sign为分隔符
	public static String[] split(String source, String sign) 
	{
		return split(source,sign.toCharArray());
	}
	//分隔字符串 参数说明:source为要分隔的串，sign为分隔符  flag=true时分隔符为与的关系
	public static String[] split(String source, String sign, boolean flag) 
	{
		String[] ret = new String[0];
		if ((source == null) || (source.equals("")))
			return ret;
 		Vector ar = new Vector();
 		String st=source; 		
 		while (true)
 		{
 			int ipos=st.indexOf(sign); 			
 			if (ipos<0)
 			{ 
 				ar.addElement(st.substring(0));
 				break;
 			}
 			else
 			{
// 				if (ipos!=0)
 				ar.addElement(st.substring(0,ipos)); 			
 				st = st.substring(ipos+sign.length());
 			}
 		}
 		ret = new String[ar.size()];
 		for (int i=0; i<ar.size(); i++)
 		{
 			ret[i] = (String)ar.elementAt(i);
 		}
		return ret;	
			
	}

	//用分隔符合成字符串 参数说明:source为要合成的字串数组，sign为分隔符
	public static String unsplit(String[] source, String sign) 
	{
		String ret="";
		if (source == null) 
			return ret;
		for (int i=0; i<source.length; i++)
		{
			if (i==0)
				ret = source[i];
			else
				ret += sign+source[i];
		}
		return ret;				
	}	
	//字符串数组转为字符串
	public static String arrayToStr(String[] source)
	{
		return unsplit(source,";");
	}
 	//是否包括索引
 	public static boolean isChecked(String all,int index)
 	{
 		String s=String.valueOf(index);
 		String s1="{"+s+" ";
 		String s2="{"+s+"}";
 		String s3=" "+s+" ";
 		String s4=" "+s+"}"; 		
 		if ((all.indexOf(s1)>=0)||(all.indexOf(s2)>=0)||(all.indexOf(s3)>=0)||(all.indexOf(s4)>=0))
 		  return true;
 		else
 		  return false; 
 	}
 	//  是否为数字串
 	public static boolean isNumeric(String s)
 	{
 		try
 		{
 			Integer.parseInt(s);
 			return true;
 		}
 		catch(Exception e)
 		{
 			return false;
 		}
 	}
 	//删除配置项匹配的记录 	
 	public static boolean delConfItem(String conffile,String item,String value)
 	{
			File f = new File(conffile);
			boolean found = false;
		    try
		    {
	 		  if (!f.exists()) f.createNewFile();
	 		  	
	 		  deleteFile(conffile+".bak");
	 		  deleteFile(conffile+".tmp");
			  FileWriter fw = new FileWriter(conffile+".tmp");			  
			  FileReader cf = new FileReader(conffile);
			  BufferedReader is = new BufferedReader(cf); 			  
			  String str = is.readLine();
			  while (str != null){
			  	if (str.equals(item+"="+value))
			  	{
			  		found = true;
					str = is.readLine();
					while ((str != null) && (!str.equals(endline)))
						str = is.readLine();
			  	}
			  	else
			    	fw.write(str+"\r\n");
			    if (str != null)
			    	str = is.readLine();
			  }
			  is.close();
			  cf.close();
			  fw.close();
			  moveFile(conffile,conffile+".bak");		  
			  moveFile(conffile+".tmp",conffile);
		    }
		    catch (Exception e)
		    {
		      System.err.println("不能读或写属性文件: "+conffile+" \n");
		      return false;
		    }
			return found;			
	}
 	//修改配置项匹配的记录 	
 	public static boolean updateConfItem(String conffile,String condition,String item,String value)
 	{
 			if ((condition==null) || (condition.equals("")))
 				return writeConf(conffile,item,value);
	  	    value = replace(value,"\r\n","</p><p>");	
			File f = new File(conffile);
			boolean found = false;
		    try
		    {
	 		  if (!f.exists()) f.createNewFile();
	 		  	
	 		  deleteFile(conffile+".bak");
	 		  deleteFile(conffile+".tmp");
			  FileWriter fw = new FileWriter(conffile+".tmp");			  
			  FileReader cf = new FileReader(conffile);
			  BufferedReader is = new BufferedReader(cf); 			  
			  String str = is.readLine();
			  while (str != null){
			  	if (str.equals(condition))
			  	{
					while ((str != null) && (!str.equals(endline)))
					{
						if (str.startsWith(item+"=",0))
						{
							str=item+"="+value;
							found=true;
						}
			    		fw.write(str+"\r\n");
						str = is.readLine();
					}
					if (!found) fw.write(item+"="+value+"\r\n");
					found = true;
					if (str != null) fw.write(str+"\r\n");
			  	}
			  	else
			    	if (str != null) fw.write(str+"\r\n");
			    if (str != null)
			    	str = is.readLine();
			  }
			  is.close();
			  cf.close();
			  fw.close();
			  moveFile(conffile,conffile+".bak");		  
			  moveFile(conffile+".tmp",conffile);
		    }
		    catch (Exception e)
		    {
		      System.err.println("不能读或写属性文件: "+conffile+" \n");
		      return false;
		    }
			return found;			
	}
 	//增加配置项 	
 	public static boolean addConfItem(String conffile,String item,String value)
 	{
 	  value = replace(value,"\r\n","</p><p>");	
 	  try 	  
 	  { 	  	
		FileWriter fw = new FileWriter(conffile,true);
		fw.write(item+"=" +  value +"\r\n");
		fw.close(); 
		return true;		
	  }
	  catch(Exception e)
	  {
	  	return false;
	  }
 	}
 	//增加分隔行
 	public static boolean addEndLine(String conffile)
 	{
 	  try 	  
 	  {
		FileWriter fw = new FileWriter(conffile,true);
		fw.write(endline +"\r\n");
		fw.close(); 		
		return true;		
	  }
	  catch(Exception e)
	  {
	  	return false;
	  }
 	}
 	//读配置文件中配置项的列表
 	public static Enumeration getConfList(String conffile,String item)
 	{
 		Vector ar = new Vector();
 		try {
 			FileReader cf=new FileReader(conffile);
  	    	BufferedReader is = new BufferedReader(cf); 			  
			String str = is.readLine();
			while (str != null){
		  		if (str.startsWith(item+"=",0))
		  		{
		  			str = str.substring(new String(item+"=").length());
					ar.addElement(str);
					str = is.readLine();
  		  		}
  		  		else
				    str = is.readLine();
 			}
 			is.close();
 			cf.close();
 		}catch (Exception ex)
 		{
 			
 		}
 		return ar.elements();
 		
 	}
 	//读取文本文件的内容
 	public static String readFile(String curfile)
 	{
			File f = new File(curfile);
		    try
		    {
	 		  if (!f.exists()) throw new Exception();
	 		  FileReader cf=new FileReader(curfile);
			  BufferedReader is = new BufferedReader(cf);
			  String filecontent = ""; 			  
			  String str = is.readLine();
			  while (str != null){
			  	filecontent += str;
		    	str = is.readLine();
		    	if (str != null)
		    		filecontent += "\n";
			  }
			  is.close();
			  cf.close();
			  return filecontent;
		    }
		    catch (Exception e)
		    {
		      System.err.println("不能读属性文件: "+curfile+" \n"+e.getMessage());
		      return "";
		    }
 		
 	}
 	//读取配置项匹配的记录值 	
 	public static String readConfItem(String conffile,String condition,String item)
 	{
 			if ((condition==null) || (condition.equals("")))
 				return readConf(conffile,item);
 			String value="";
			File f = new File(conffile);
			boolean found = false;
		    try
		    {
	 		  if (!f.exists()) return "";
	 		  FileReader cf=new FileReader(conffile);
			  BufferedReader is = new BufferedReader(cf); 			  
			  String str = is.readLine();
			  while (str != null){
			  	if (str.equals(condition))
			  	{
					while ((str != null) && (!str.equals(endline)))
					{
						if (str.startsWith(item+"=",0))
						{
							found=true;
				  			value = str.substring(new String(item+"=").length());
							str = null; //找到，结束
						}
						else
							str = is.readLine();
					}
					str = null;   //找到，结束
			  	}
			    if (str != null)
			    	str = is.readLine();
			  }
			  is.close();
			  cf.close();
		    }
		    catch (Exception e)
		    {
		      System.err.println("不能读属性文件: "+conffile+" \n"+e.getMessage());
		      return "";
		    }
			return replace(value,"</p><p>","\r\n");			
	}
	//读配置文件的项
	public static String readConf(String conffile,String item)
	{
			String ret="";
			Properties myProps = new Properties();
		    try
		    {
			  InputStream is = new FileInputStream(conffile);
		      myProps.load(is);
		      ret=myProps.getProperty(item,"").trim();
		      is.close();		      
		    }
		    catch (Exception e)
		    {
		      System.err.println("不能读取属性文件: "+conffile+" \n");
		      return "";
		    }
		    
		    return decodeGB( replace(ret,"</p><p>","\r\n"));			
		    
	}
	//写配置文件的项
	public static boolean writeConf(String conffile, String item, String value)
	{
		 	value = replace(value,"\r\n","</p><p>");	
			File f = new File(conffile);
			boolean found = false;
		    try
		    {
	 		  if (!f.exists()) f.createNewFile();
	 		  deleteFile(conffile+".bak");
	 		  deleteFile(conffile+".tmp");
			  FileWriter fw = new FileWriter(conffile+".tmp");			  
			  FileReader cf=new FileReader(conffile);
			  BufferedReader is = new BufferedReader(cf); 			  
			  String str = is.readLine();
			  while (str != null){
			  	if (str.startsWith(item+"=",0))
			  	{
			  		str=item+"="+value;
			  		found = true;
			  	}
			    fw.write(str+"\r\n");
			    str = is.readLine();
			  }
			  if (!found)
			  { 
			  	str=item+"="+value;
				fw.write(str+"\r\n");
			  }	
			  is.close();
			  cf.close();
			  fw.close();			  
			  if (false) //**** 
			  {
			    log.info(item+"="+value);
			  	log.info("rename file "+conffile+" to "+conffile+".bak");
			  	log.info(moveFile(conffile,conffile+".bak"));
			  	log.info("rename file "+conffile+".tmp to "+conffile);
			  	log.info(moveFile(conffile+".tmp",conffile));
			  }
		    }
		    catch (Exception e)
		    {
		      if (false) System.err.println("不能读或写属性文件: "+conffile+" \n");
		      return false;
		    }
			return true;			
	}
	//读config配置文件
	public static String readDomainCfg(String confpath, String item)
	{
			String conffile = confpath+"/"+configfile;
  		    return readConf(conffile,item);			
	}
	//读config配置文件
	public static String readDomainCfg(String confpath, String item, String defaultvalue)
	{
			String ret = readDomainCfg(confpath,item);			
			if ((ret==null)||(ret.trim().length()==0))
				ret = defaultvalue;
			return ret;
	}
	//写config配置文件
	public static boolean writeDomainCfg(String confpath, String item, String value)
	{
			String conffile = confpath+"/"+configfile;
			return writeConf(conffile,item,value);		    
	}
	//读config配置文件
	public static String readTemplateCfg(String confpath, String item)
	{
			String conffile = confpath+"/"+configfile;
  		    return readConf(conffile,item);			
	}
	//读config配置文件
	public static String readTemplateCfg(String confpath, String item, String defaultvalue)
	{
			String ret = readTemplateCfg(confpath,item);			
			if ((ret==null)||(ret.trim().length()==0))
				ret = defaultvalue;
			return ret;
	}
	//写config配置文件
	public static boolean writeTemplateCfg(String confpath, String item, String value)
	{
			String conffile = confpath+"/"+configfile;
			return writeConf(conffile,item,value);		    
	}
	//文件是否存在
	public static boolean fileExists(String filename)
	{
			File f=new File(filename);
			return f.isFile()&&f.exists();
			
	}
	//目录是否存在
	public static boolean folderExists(String pathname)
	{
			File f=new File(pathname);
			return f.isDirectory()&&f.exists();
	}
	
	//删除指定的文件
	public static boolean deleteFile(String filename)
	{
			File f=new File(filename);
			return f.isFile()&&f.delete();
	}
	//删除指定的文件或空目录
	public static boolean deleteFileEx(String fileorpath)
	{
			File f=new File(fileorpath);
			return f.delete();
	}
	//删除指定的目录及其目录中的文件
	public static boolean deletePath(String pathname)
	{
			File f=new File(pathname);
			if (!f.isDirectory()) 
				return false;
			String[] listfile=f.list();
			if (listfile != null)
				for (int i=0; i<listfile.length; i++)
				{
					if (!deleteFileEx(pathname+"/"+listfile[i]))
						return false;
				}			
			return f.delete();
	}
	//删除指定的目录（含所有子目录）及其目录中的文件
	public static boolean deleteTree(String pathname)
	{
			File f=new File(pathname);
			String[] listfile=f.list();
			if (listfile != null)
				for (int i=0; i<listfile.length; i++)
				{
					deleteTree(pathname+"/"+listfile[i]);
				}			
			return f.delete();
	}
	//更改文件名或目录名
	public static boolean moveFile(String sfilename, String dfilename)
	{
			File fs=new File(sfilename);
			File fd=new File(dfilename);			
			return fs.renameTo(fd);
	}
	//创建文件
	public static boolean createFile(String filename)
	{
			File f=new File(filename);
			try{
				return f.createNewFile();
			}
			catch (Exception e)	{
				return false;
			}

	}
	//创建目录
	public static boolean mkdir(String pathname)
	{
			File f=new File(pathname);
			try{
				return f.mkdir();
			}
			catch (Exception e)	{
				return false;
			}

	}
	//创建目录(含不存在的上级及上上级等目录)
	public static boolean mkdirs(String pathname)
	{
			File f=new File(pathname);
			try{
				return f.mkdirs();
			}
			catch (Exception e)	{
				return false;
			}

	}
	//复制文件
	public static boolean copyFile(String source, String desc)
	{
		try
		{
			File fl = new File(source);
			int length = (int)fl.length();
		  	FileInputStream is = new FileInputStream(source);
		  	FileOutputStream os = new FileOutputStream(desc);
			byte[] b = new byte[length];
		  	is.read(b);
		  	os.write(b);
		  	is.close();
		  	os.close();
		  	return true;
	  	} 
	  	catch (Exception e)
	  	{
	  		return false;
	  	}
	}

	//取文件大小
	public static int getFileSize(String filename)
	{
		try
		{
			File fl = new File(filename);
			int length = (int)fl.length();
			return length;
	  	} 
	  	catch (Exception e)
	  	{
	  		return 0;
	  	}
		
	}
	
	//取文件全称不含路径的文件名
	public static String getFileName(String filePathName)
	{
	    int pos = 0;
	    pos = filePathName.lastIndexOf(47); //  "/"
	    if(pos != -1)
	        return filePathName.substring(pos + 1, filePathName.length());
	    pos = filePathName.lastIndexOf(92); //  "\"
	    if(pos != -1)
	        return filePathName.substring(pos + 1, filePathName.length());
	    else
	        return filePathName;
	}
	//取文件的扩展名
	public static String getFileExt(String filePathName)
	{
	    int pos = 0;
	    pos = filePathName.lastIndexOf('.');
	    if(pos != -1)
	        return filePathName.substring(pos + 1, filePathName.length());
	    else
	        return "";
		
	}
	//取文件全称的路径
	public static String getFilePath(String filePathName)
	{
	    String token = new String();
	    String value = new String();
	    int pos = 0;
	    int i = 0;
	    int start = 0;
	    int end = 0;
	    pos = filePathName.lastIndexOf(47);
	    if(pos != -1)
	        return filePathName.substring(0,pos);
	    pos = filePathName.lastIndexOf(92);
	    if(pos != -1)
	        return filePathName.substring(0,pos);
	    else
	        return filePathName;
	}

	
	//判断字串desc是否存在于source，存在则返回true,不存在返回false
	public static boolean inStr(String source,String desc)
	{
			if (source.indexOf(desc)>=0) 
				return true;
			else return false;
	}
	//取两整数相除最大值
	public static int celling(int a,int b)
	{
			
			int c = a/b;
			if (a!=b*c) c=c+1;
			return c;
	}
	//取余数
	public static int mod(int a,int b)	
	{
			return a%b;
	}
	//字符串内码转换--用于写入数据库时
	public static String encodeGB(String source,String charset)
	{
		if (source == null) return "";
		  try
		  {
		    return (new String(source.getBytes("GB2312"),charset));
		  }
		  catch(Exception e)
		  {
		  	return source;
		  }
	}
	//字符串内码转换--用于写入数据库时
	public static String encodeGB(String source)
	{
		return encodeGB(source, "ISO-8859-1");
	}

	//字符串内码转换--用于从数据库读取数据时	
	public static String decodeGB(String source,String charset)
	{
		if (source == null) return "";
		try{
		    return (new String(source.getBytes(charset),"GB2312"));
		}catch (Exception E)
		{
		  	return source;
		}
		  
	}
	//字符串内码转换--用于从数据库读取数据时	
	public static String decodeGB(String source)
	{
		return decodeGB(source, "ISO-8859-1");
	}

	//字符串内码转换--用于从数据库读取数据时	
	public static String autoDecodeGB(String source)
	{
		if (source == null) source = "";
		String encode = encodeGB(source);		
		if (source.equals(decodeGB(encode)))
		{
			return source;
		}
		return decodeGB(source);
	}
		
	//URL编码
	public static String urlEncode(String source)
	{
		if (source == null) source = "";
		return java.net.URLEncoder.encode(source.trim());
	}
	//URL解码
	public static String urlDecode(String source)
	{
		if (source == null) source = "";
		return java.net.URLDecoder.decode(source.trim());
	}
	
	//转换为HTML格式用来显示，即 '&'转为&amp; ' '转为&nbsp; '"'转为&quot; '<'转为&lt; '>'转为&gt;
	//\n=><br>
	public static String toHtmlStr(String s)
	{
		if ( s == null)
			return ""; //原为"&nbsp"
		else 
			return replace(replace(replace(replace(replace(replace(s,'&',"&amp;"),' ',"&nbsp;"),'"',"&quot;"),'<',"&lt;"),'>',"&gt;"),'\n',"<br>");
	}
	//转换为HTML格式用来显示，即 '&'转为&amp; ' '转为&nbsp; '"'转为&quot; '<'转为&lt; '>'转为&gt;
	//\n不转为<br>
	public static String toHtmlMemo(String s)
	{
		if ( s == null)
			return ""; //原为"&nbsp"
		else 
			return replace(replace(replace(replace(replace(s,'&',"&amp;"),' ',"&nbsp;"),'"',"&quot;"),'<',"&lt;"),'>',"&gt;");
	}
	//转换为HTML格式用来显示，即 '&'转为&amp; ' '转为&nbsp; '"'转为&quot; '<'转为&lt; '>'转为&gt;
	public static String toHtmlStr(Object ob)
	{
		String s = (String)ob;
		if ( s == null)
			return ""; //原为"&nbsp"
		else 
			return toHtmlStr(s);
	}

	//空或空格转换为HTML格式用来显示，
	public static String toHtmlNull(Object ob)
	{
		String s = (String)ob;
		if ( s == null || s.trim().equals(""))
			return "&nbsp;";
		else 
			return s;
	}
	
	//转换html中的script代码，不让其执行
	public static String toHtmlScript(String s)
	{
		if ( s == null)
			return ""; 
		else
		{
			s = replace(s,"</script>","&lt;/script&gt;");
			s = replace(s,"<script","&lt;script");
			s = replace(s,"\n","<br>");
			return s;			
		}
	}

	//字符串转换，即NULL转为"",非空则不变
	public static String nullToStr(String s)
	{
			if ( s == null || s.trim().length()<0)
				return "";
			else 
				return s;
	}
	
	//替换source中的str1为str2
	public static String replace(String source,char str1,String str2)
	{
		return replace(source,String.valueOf(str1),str2);
	}
	//替换source中的str1为str2
	public static String replace(String source,String str1,String str2)
	{
		if (source==null)	
			return "";
		String desc = "";
		int posstart = 0;
		int posend = source.indexOf(str1,0);
		int len = source.length();
		while (posend>=0 && posstart<len)
		{
			desc += source.substring(posstart,posend)+str2;
			posstart = posend+str1.length();
			posend = source.indexOf(str1,posstart);
		}
		if (posstart < len) 
			desc += source.substring(posstart,len);
		return desc;
	}
	//转换字符串用于SQL串中（把'=>''）
	public static String toSqlStr(String source)
	{			
			if (source == null) return "";
			return replace(source,'\'',"\'\'");
	}

	
	//转换字符串用于SQL串中（把'=>"）
	public static String toSqlStr(String source,int flag)
	{			
			source = toSqlStr(source);
			source = "'"+source+"'";
			if (flag == 0)
				return ","+source;
			else
				return source;
			
	}		
		//判断字符串是否为空
    public static boolean isNullStr(String s)
    {
    	if (s==null || s.trim().length()<=0)
    		return true;
    	else return false; 
    } 

		//判断字符串数组是否为空
    public static boolean isNullStr(String[] s)
    {
    	if ((s==null) || (s.length<=0))
    		return true;
    	else return false; 
    } 
    
    //按字段的字段查询值加条件（加LIKE）
	public static String strLike(String fieldValue,String field)
	{
			if (isNullStr(fieldValue))
				return " ";
			else
				return " and "+field+" like '%"+toSqlStr(fieldValue.trim())+"%' ";
	}
    //按字段的字段查询值加条件（加LIKE）
	public static String strOrLike(String fieldValue,String field)
	{
			if (isNullStr(fieldValue))
				return " ";
			else
				return " or "+field+" like '%"+toSqlStr(fieldValue.trim())+"%' or '"+toSqlStr(fieldValue.trim())+"' like '%'+"+field+"+'%' ";
	}

	//按长度把字符串前补0
	public static String strLen(String s, int len)
	{
			if (isNullStr(s)) 
				s="";
			for (int i=0;i<len-s.length();i++)
			{
				s = "0"+s;
			}
			return s;
	}
	//按长度把字符串后补0
	public static String strLen2(String s, int len)
	{
			if (isNullStr(s)) 
				s="";
			for (int i=0;i<len-s.length();i++)
			{
				s = s+"0";
			}
			return s;
	}
	//取查询选择项
	public static String getInputParam(String[] query_type)
	{
		String s = "";
		if (query_type != null)
		  for (int i=0;i<query_type.length;i++)
				s = s+"<input type='hidden' name='query_type' value='"+query_type[i]+"'>";
		return s;
	}
	//取查询选择项
	public static String getInputField(String inputfield,String inputvalue)
	{
		if (inputvalue != null) 
			return "<input type='hidden' name='"+inputfield+"'"+" value='"+inputvalue+"'>";
		else return "<input type='hidden' name='"+inputfield+"'>";
	}
	//取字符串字段的字段数
	public static int getFieldCount(String fields)
	{
		if (fields.trim() == "") return 0;
		int i=0,count=0;
		while (fields.indexOf(",",i) != -1)
		{
			count++;
			i = fields.indexOf(",",i)+1;
		}
		return count;	
	}

	//取日期字符串的时间的整型值
	public static long getTime(Object time)
	{
		return getCal(toString(time)).getTime().getTime();
	}
	//返回统一格式的日期
	public static String getLegalDate(String year,String month,String day,String hour,String minute,String secound)
	{
		int iyear = parseInt(year);
		int imonth = parseInt(month)-1;
		int iday = parseInt(day);
		int ihour = parseInt(hour);
		int iminute = parseInt(minute);
		int isecound = parseInt(secound);
		if (iyear<1700)  iyear = 1700;
		if (iday < 1) iday = 1;
		Calendar cal = Calendar.getInstance();
		cal.set(iyear,imonth,iday,ihour,iminute,isecound);
		return getDate(cal);
	}
	//返回统一格式的日期	
	public static String getLegalDate(String year, String month, String day)
	{
		return getLegalDate(year,month,day,"0","0","0");
	}
		
	//返回日历的年字符串
	public static String getYear(Calendar cal)
	{
			return String.valueOf(cal.get(cal.YEAR));
	}
	//返回日历的月字符串(两位)
	public static String getMonth(Calendar cal)
	{
			return strLen(String.valueOf(cal.get(cal.MONTH)+1),2);
	}
	//返回日历的日字符串(两位)
	public static String getDay(Calendar cal)
	{
			return strLen(String.valueOf(cal.get(cal.DAY_OF_MONTH)),2);
	}
	//返回日历的时字符串(两位)
	public static String getHour(Calendar cal)
	{
			return strLen(String.valueOf(cal.get(cal.HOUR_OF_DAY)),2);
	}
	//返回日历的分字符串(两位)
	public static String getMinute(Calendar cal)
	{
			return strLen(String.valueOf(cal.get(cal.MINUTE)),2);
	}		
	//返回日历的秒字符串(两位)		
	public static String getSecond(Calendar cal)
	{
			return strLen(String.valueOf(cal.get(cal.SECOND)),2);
	}

	//返回日历的日期字符串（格式："yyyy/mm/dd"）
	public static String getDateStr(Calendar cal)
	{
      return getYear(cal)+"/"+getMonth(cal)+"/"+getDay(cal);
	}
	//返回日历的日期字符串（格式："yyyy/mm/dd"）
	public static String getDateStr(String date)
	{
      return getDateStr(getCal(date));
	}
	//返回日历的时间字符串（格式："hh:ss:mm"）
	public static String getTimeStr(Calendar cal)
	{
      return getHour(cal)+":"+getMinute(cal)+":"+getSecond(cal);
	}
	//返回日历的时间字符串（格式："hh:ss:mm"）
	public static String getTimeStr(String date)
	{
      return getTimeStr(getCal(date));
	}
	//返回日历的日期时间字符串（格式："yyyy/mm/dd hh:ss:mm"）
	public static String getDate(Calendar cal)
	{
  		return getDateStr(cal)+" "+getTimeStr(cal);
	}
	//返回日历的日期时间字符串（格式："yyyy/mm/dd hh:ss:mm"）
	public static String getDate(Date dat)
	{
		if (dat==null) return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
  		return getDateStr(cal)+" "+getTimeStr(cal);
	}
	//返回日期字符串("yyyy/mm/dd hh:ss:mm")的年
	public static int getYear(String s)
	{
		try
		{
			return Integer.parseInt(s.substring(0,4));
		}
		catch(Exception e)
		{
			return 1970;
		}
	}
	//返回日期字符串("yyyy/mm/dd hh:ss:mm")的月
	public static int getMonth(String s)
	{
		try
		{
			return Integer.parseInt(s.substring(5,7));
		}
		catch(Exception e)
		{
			return 1;
		}
	}
	//返回日期字符串("yyyy/mm/dd hh:ss:mm")的日
	public static int getDay(String s)
	{
		try
		{
			return Integer.parseInt(s.substring(8,10));
		}
		catch(Exception e)
		{
			return 1;
		}
	}
	//返回日期字符串("yyyy/mm/dd hh:ss:mm")的时
	public static int getHour(String s)
	{
		try
		{
			return Integer.parseInt(s.substring(11,13));
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	//返回日期字符串("yyyy/mm/dd hh:ss:mm")的分
	public static int getMinute(String s)
	{
		try
		{
			return Integer.parseInt(s.substring(14,16));
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	//返回日期字符串("yyyy/mm/dd hh:ss:mm")的秒
	public static int getSecond(String s)
	{
		try
		{
			return Integer.parseInt(s.substring(17,19));
		}
		catch(Exception e)
		{
			return 0;
		}
	}
		
	//返回日期时间字符串对应的日历（格式："yyyy/mm/dd hh:ss:mm"）
	public static Calendar getCal(String s)
	{
			Calendar cal=Calendar.getInstance();
			cal.set(getYear(s),getMonth(s)-1,getDay(s),getHour(s),getMinute(s),getSecond(s));	
			return cal;
	}
	//返回日期时间字符串对应的日历（格式："yyyymmdd"）
	public static Calendar getCal2(String s)
	{
			Calendar cal=Calendar.getInstance();	
			cal.set(parseInt(s.substring(0,4)),parseInt(s.substring(4,6))-1,parseInt(s.substring(6,8)));	
			return cal;
	}
	//返回日期时间字符串对应的SQL日期（格式："yyyy/mm/dd hh:ss:mm"）ok!!!
	public static java.sql.Date getSqlDate(String str)
	{
			//return java.sql.Date.valueOf(s);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date d = null;
		try
		{
		    d = format.parse(str);	
		}
		catch(Exception e)
		{
		    e.printStackTrace();	
		}
		if (d == null)
		    return null;
		java.sql.Date date = new java.sql.Date(d.getTime());
		if (date == null)
		{    
		    return null;
		}
		return date;
	}
	//返回当天日期对应的SQL日期（）
	public static java.sql.Date getSqlDate()
	{
			return java.sql.Date.valueOf(getNowDate());
	}		
	//取当前日期时间的字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getNow()
	{
			Calendar now=Calendar.getInstance();
      		return getDateStr(now)+" "+getTimeStr(now);
	}
	//取当前日期的字符串，格式为"yyyy/mm/dd"
	public static String getNowDate()
	{
			Calendar now=Calendar.getInstance();
     		return getDateStr(now);
	}
	//取当前时间的字符串，格式为"hh:ss:mm"
	public static String getNowTime()
	{
			Calendar now=Calendar.getInstance();
      		return getTimeStr(now);
	}
	//取下一小时字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getNextHour()
	{
			Calendar now=Calendar.getInstance();
			now.set(now.HOUR, now.get(now.HOUR)+1);
      		return getDateStr(now)+" "+getTimeStr(now);
	
	}
	//取日期的下一小时字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getNextHour(String date)
	{
			Calendar now = getCal(date);
			now.set(now.HOUR, now.get(now.HOUR)+1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}	
	//取上一小时字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getBeforeHour()
	{
			Calendar now = Calendar.getInstance();
			now.set(now.HOUR, now.get(now.HOUR)-1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}	
	//取日期的上一小时字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getBeforeHour(String date)
	{
			Calendar now = getCal(date);
			now.set(now.HOUR, now.get(now.HOUR)-1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}
	//取明天字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getNextDay()
	{
			Calendar now=Calendar.getInstance();
			now.set(now.DATE, now.get(now.DATE)+1);
      		return getDateStr(now)+" "+getTimeStr(now);
	
	}
	//取日期的第二天字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getNextDay(String date)
	{
			Calendar now = getCal(date);
			now.set(now.DATE, now.get(now.DATE)+1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}	
	//取上一天字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getBeforeDay()
	{
			Calendar now = Calendar.getInstance();
			now.set(now.DATE, now.get(now.DATE)-1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}	
	//取日期的上一天字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getBeforeDay(String date)
	{
			Calendar now = getCal(date);
			now.set(now.DATE, now.get(now.DATE)-1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}
	//取本周的第一天
	public static String getThisWeek()
	{
			Calendar now=Calendar.getInstance();
			now.set(now.DATE, now.get(now.DATE)-now.get(now.DAY_OF_WEEK)+1);
      		return getDateStr(now)+" "+getTimeStr(now);			
	}
	//取本月的第一天
	public static String getThisMonth()
	{
			Calendar now=Calendar.getInstance();
			now.set(now.DATE, 1);
      		return getDateStr(now)+" "+getTimeStr(now);			
	}
	//取日期的本月的第一天
	public static String getThisMonth(String date)
	{
			Calendar now=getCal(date);
			now.set(now.DATE, 1);
      		return getDateStr(now)+" "+getTimeStr(now);			
	}
	//取日期的本周的第一天
	public static String getThisWeek(String date)
	{
			Calendar now=getCal(date);
			now.set(now.DATE, now.get(now.DATE)-now.get(now.DAY_OF_WEEK)+1);
      		return getDateStr(now)+" "+getTimeStr(now);			
	}
	//取下周的第一天
	public static String getNextWeek()
	{
			Calendar now=Calendar.getInstance();
			now.set(now.DATE, now.get(now.DATE)-now.get(now.DAY_OF_WEEK)+1+7);
      		return getDateStr(now)+" "+getTimeStr(now);			
	}
	//取日期的下周的第一天
	public static String getNextWeek(String date)
	{
			Calendar now=getCal(date);
			now.set(now.DATE, now.get(now.DATE)-now.get(now.DAY_OF_WEEK)+1+7);
      		return getDateStr(now)+" "+getTimeStr(now);			
	}
	
	//取下一月字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getNextMonth()
	{
			Calendar now=Calendar.getInstance();
			now.set(now.MONTH, now.get(now.MONTH)+1);
      		return getDateStr(now)+" "+getTimeStr(now);
	
	}
	//取日期的下一月字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getNextMonth(String date)
	{
			Calendar now = getCal(date);
			now.set(now.MONTH, now.get(now.MONTH)+1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}
	//取上一月字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getBeforeMonth()
	{
			Calendar now = Calendar.getInstance();
			now.set(now.MONTH, now.get(now.MONTH)-1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}	
	//取日期的上一月字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getBeforeMonth(String date)
	{
			Calendar now = getCal(date);
			now.set(now.MONTH, now.get(now.MONTH)-1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}
	//取下一年字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getNextYear()
	{
			Calendar now=Calendar.getInstance();
			now.set(now.YEAR, now.get(now.YEAR)+1);
      		return getDateStr(now)+" "+getTimeStr(now);
	
	}
	//取日期的下一年字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getNextYear(String date)
	{
			Calendar now = getCal(date);
			now.set(now.YEAR, now.get(now.YEAR)+1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}
	//取上一年字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getBeforeYear()
	{
			Calendar now = Calendar.getInstance();
			now.set(now.YEAR, now.get(now.YEAR)-1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}	
	//取日期的上一年字符串，格式为"yyyy/mm/dd hh:ss:mm"
	public static String getBeforeYear(String date)
	{
			Calendar now = getCal(date);
			now.set(now.YEAR, now.get(now.YEAR)-1);
      		return getDateStr(now)+" "+getTimeStr(now);
	}
	//取当前日期的前几天日期
	public static String getBeforeDay(int day)
	{
			Calendar now = Calendar.getInstance();
			now.set(now.DATE, now.get(now.DATE)-day);
			return getDate(now);
	}
	//本地字符转为unicode
	public static String native2Unicode(String s) 
	{
            if (s == null || s.length() == 0) {
               return null;
       	    }
            byte[] buffer = new byte[s.length()];
            for (int i = 0; i < s.length(); i++) {
               buffer[i] = (byte)s.charAt(i);
            }
            return new String(buffer);
    }       
	//unicode字符转为本地
	public static String unicode2Native(String s) 
	{
           if (s == null || s.length() == 0) {
              return null;
           }
           char[] buffer = new char[s.length() * 2];
           char c;
           int j = 0;
           for (int i = 0; i < s.length(); i++) {
              if (s.charAt(i) >= 0x100) {
                 c = s.charAt(i);
                 byte []buf = (""+c).getBytes();
                 buffer[j++]  = (char)buf[0];
                 buffer[j++]  = (char)buf[1];
              }
              else {
                 buffer[j++] = s.charAt(i);
              }
	   }
           return new String(buffer, 0, j);
	}
	//输出中国风格的日期
	public static String getChnDate(String computerDate)
	{
		String out= String.valueOf(getYear(computerDate))+"年"+String.valueOf(getMonth(computerDate))+"月"+String.valueOf(getDay(computerDate))+"日";
		return out;	
	}
}