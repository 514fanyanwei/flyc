package cc.gps.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.active.jt808.JTProcess0x0102;

public class Ecode {
	private static final Log log = LogFactory.getLog(Ecode.class);
	//16进制字符串形(低位在前，高位在后)式转为10进制字符串形式 例:"012A"---->"298"  1*256+2*16+10
	public static String HEX2DECLH(String hex)
	{
		StringBuffer result=new StringBuffer();
		for(int i=0;i<hex.length();i=i+2){
			result.insert(0, hex.substring(i, i+2));
		}
		return Long.valueOf(result.toString(),16).toString();
	}
	//10进制整数转为16进制字符串形式(低位在前，高位在后)  例:298---->"012A" (ok)
	public static String DEC2HEXLH(long dec)
	{
		String hex=Long.toHexString(dec).toString();
		if(hex.length()%2!=0) hex="0"+hex;
		StringBuffer result=new StringBuffer();
		for(int i=0;i<hex.length();i=i+2){
			result.insert(0, hex.substring(i, i+2));
		}
		return result.toString();
	}
	//16进制字符串形式(高位在前)转为10进制字符串形式 例:"012A"---->"298"  1*256+2*16+10
	public static String HEX2DEC(String hex)
	{
		return Integer.valueOf(hex,16).toString();
	}
	public static String HEX2DEC(String hex,int n)
	{
		String s=Integer.valueOf(hex,16).toString();
		while(s.length()<n) s="0"+s;
		return s;
	}
	//16进制按2位一组转十进制后的字符串  //2014-5-15
	//"14000101"  ----> "20000101"
	public static String BCD2DEC(String hex){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<hex.length();){
			String s=Integer.valueOf(hex.substring(i,i+2),16).toString();
			if(s.length()<2) s="0"+s;
			i+=2;
			sb.append(s);
		}
		return sb.toString();
	}
	
	//10进制按2位一组转十六进制后的字符串  //2014-5-15
	//"20000101"  ------->"14000101" 
	public static String DEC2BCD(String dec){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<dec.length();){
			String s=Integer.toHexString(Integer.parseInt(dec.substring(i,i+2)));
			//Integer.valueOf(dec.substring(i,i+2)).toString();
			if(s.length()<2) s="0"+s;
			i+=2;
			sb.append(s);
		}
		return sb.toString();
	}
	
	//10进制整数转为16进制字符串形式  例:298---->"012A" (ok)
	public static String DEC2HEX(long dec)
	{
		String hex=Long.toHexString(dec).toString();
		if(hex.length()%2!=0) hex="0"+hex;
		return hex;
	}
	//10进制整数转为指定长度的16进制字符串形式  例:(298,8)---->"0000012A" (ok)
		public static String DEC2HEX(int dec,int len)
		{
			if(dec<0)  dec=256+dec;
			String hex=Integer.toHexString(dec).toString();
			while(hex.length()<len)
				hex="0"+hex;
			return hex;
		}
	////10进制长整数转为指定长度的16进制字符串形式 (ok)
		public static String DEC2HEX(long dec,int len)
		{
			String hex=Long.toHexString(dec).toString();
			while(hex.length()<len)
				hex="0"+hex;
			return hex;
		}

	
	//10进制字节数组按每个分量转为16进制后原样连接为字符串 例:byte[] bs2={3,-1,18,10}; ---> 03FF120A
	//public static String IntArray2String(byte[] src,int start,int end)
	public static String ByteArray2HexString(byte[] src,int start,int end)
	{
		if (src == null || src.length <= 0) {
			return null;
		}
		if((start==-1)&&(end==-1)) {start =0;end=src.length-1;}
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = start; i <=end; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	//16进制字节数组按每个分量转为10进制后原样连接为字符串 
	// 例 bs={0x02,0x1a,0x03,0x56} --->02260386   0x02-->02 0x1a->26....
	public static String Byte16ArrayDecString(byte[] src,int start,int end)
	{
		if (src == null || src.length <= 0) {
			return null;
		}
		if((start==-1)&&(end==-1)) {start =0;end=src.length-1;}
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = start; i <=end; i++) {
			
			String hv=String.valueOf(src[i]);
			if ((hv.length() < 2)) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	/**  
     * 16进制字符串每2个一组转10进制, "FFIA"--->-1 ,26 即 每2个字符一组 作为16进制看 0xFF --->-1  01A--->26  
     * @param b  
     * @return  
     */ 
    public static byte[] HexString2ByteArray(String src) {
    	
    	byte[] b= src.getBytes();
        if ((b.length % 2) != 0) {  
           // throw new IllegalArgumentException("长度不是偶数");
        	
        }  
        byte[] b2 = new byte[b.length / 2];  
        int k=0;
        for (int n = 0; n < b.length; n += 2) {  
        	String item = new String(b, n, 2);
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节  
            b2[k++] = (byte) Integer.parseInt(item, 16);  
        }  
        b = null;  
        return b2;  
    }
   
    
    //字符串中每个字符当作1个16进制转成4位二进制  (ok)
    public static String hexString2binaryString(String hexString)
	{
		if (hexString == null )
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++)
		{
			tmp = "0000"
					+ Integer.toBinaryString(Integer.parseInt(hexString
							.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}
    //将bString的二进制字符串按每4位二进制转1个BCD码(必须是4的整数倍),
    public static String binaryString2BCDString(String bString)
	{
		if (bString == null || bString.equals("") || bString.length() % 4 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4)
		{
			iTmp = binToDec(bString.substring(i,i+4));
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}
    
    //每4位二进制转1个16进制字符(必须是4的整数倍),
    public static String binaryString2hexString(String bString)
	{
		if (bString == null || bString.equals("") || bString.length() % 4 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4)
		{
			iTmp = 0;
			for (int j = 0; j < 4; j++)
			{
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}
    //求二进制字符串的10进制值
    public static int binToDec(String bin) 
    {
    	int dec = (new BigInteger(bin, 2)).intValue();//转换为BigInteger类型
    	return dec;
    }
    //1个10进制->至少8位2进制 (ok) 
    public static String decToBin(long dec){
    	BigInteger bin = new BigInteger(String.valueOf(dec));//转换为BigInteger类型 
    	String s=bin.toString(2);
    	while(s.length()<8) s="0"+s;
    	return s;
    }
    //1个10进制数 -->转为8位2进制 --->每4位二进制转1个bcd码　再连接为字符串 (ok)
    public static String decToBCD(int dec){
    	String s=decToBin(dec);
    	return binToDec(s.substring(0, 4))+""+binToDec(s.substring(4));
    	//return binToDec(s.substring(4))+"";
    }
  //1个10进制数 -->转为4位2进制 --->4位二进制转1个bcd码　再连接为字符串 (ok)
    public static String decTo4BCD(int dec){
    	String s=decToBin(dec);
    	//return binToDec(s.substring(0, 4))+""+binToDec(s.substring(4));
    	return binToDec(s.substring(4))+"";
    }
    
	//随机生成用16进制表示的2个字节数组(弃用)
    public static byte[] createOrderID_old()
	{
		byte[] b=new byte[2];
		int random=(int)(Math.random()*65536);
		String hex=DEC2HEX(random);
		if(hex.length()<4)
		while(hex.length()<4)
			hex="0"+hex;
        log.info("random="+random+"   hex="+hex);
        b=HexString2ByteArray(hex);
		return b;
	}
  //随机生成用16进制表示的4位长字符串
    public static int createSerialID()
	{
		int random=(int)(Math.random()*65535);
		//String hex=DEC2HEX(random);
		//if(hex.length()<4)
		//while(hex.length()<4)
			//hex="0"+hex;
		return random;
	}
	//int转java字节码  //----> 16进制--->整理为偶数个数位(前端添０)-->每2位1组组成16进制字节数组.(//转为10进制-->转为byte类型(考虑超128的数)--->加入byte[]数组)
    public static byte[] intToBytes(int n)
	{
    	String hex=DEC2HEX(n);
    	log.info(hex);
    	return hex2byte(hex.getBytes());
	}
    /**  
     * 16进制字符串转java字节码  
     * @param b  
     * @return  
     */ 
    public static byte[] hex2byte(byte[] b) {  
        if ((b.length % 2) != 0) {  
            throw new IllegalArgumentException("长度不是偶数");  
        }  
        byte[] b2 = new byte[b.length / 2];  
        for (int n = 0; n < b.length; n += 2) {  
            String item = new String(b, n, 2);  
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节  
            b2[n / 2] = (byte) Integer.parseInt(item, 16);  
        }  
        b = null;  
        return b2;  
    }
 
    //异或校验
    public static int buildCheckCode(String str){
		int xor=0;
		for(int i=0;i<str.length();i+=2){
			//log.info(str.substring(i,i+2)+" - ");
			int sn=Integer.parseInt(str.substring(i,i+2),16);
			try{
			//xor^=Integer.parseInt(str.substring(i,i+2),16);
				xor^=sn;
				//if(sn==0x7d) xor^=0x01;  //因为转义后要加上
				//if(sn==0x7e) xor^=0x02;
					
			}catch(Exception e){};
		}
		//for(int i=0;i<bs.length;i++){
			//xor^=bs[i];
		//}
		return xor;
	}
    //ASCII值(16进制)字符串--->转字符串  "303132"--->"012"
    /*
    public static String A2S(String asc){
    	log.info(asc+"--------");
    	if(asc.length()%2!=0) return "";
    	StringBuffer sb = new StringBuffer();
    	for(int i=0;i<asc.length();i=i+2){
    		byte b=Byte.parseByte((String) asc.subSequence(i, i+2),16);
    		sb.append((char)b);
    	}
    	return sb.toString();
    }*/
    public static String A2S(String asc){
    	if(asc.length()%2!=0) return "";
    	StringBuffer sb = new StringBuffer();
    	int k=0;
    	byte[] bs= new byte[asc.length()/2];
    	for(int i=0;i<asc.length()-1;i+=2){
			
			bs[k++]=(byte)Integer.parseInt(asc.substring(i,i+2),16);
		}
    	String s="";
		try {
			s = new String(bs,"GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return  s;
    }
    //字符串--->ASCII值(16进制)字符串  "012"---> "303132"
    public static String S2A(String str){
    	if(str==null) return "";
    	try{
    		byte[] bs=str.getBytes("GBK");
    		str=ByteArray2HexString(bs,-1,-1);
    	}catch(Exception e){}
    	return str;
    	
    	/*
    	if(str==null) return "";
    	StringBuffer sb = new StringBuffer();
    	for(int i=0;i<str.length();i++){
    		byte b=(byte)(str.charAt(i));
    		sb.append(DEC2HEX(b));
    	}
    	return sb.toString();*/
    }

	public static void main(String args[])
	{
		
		log.info(Ecode.S2A("SaaTbbbccdd"));
		log.info(Ecode.BCD2DEC("14000101"));  
		log.info(Ecode.DEC2BCD("20000112"));
		log.info(Ecode.A2S("32303134303530"));
		
		String ip=Ecode.S2A("218.70.24.166");
		while(ip.length()<40){
			ip+="00";
		}
		log.info(ip);
		log.info(Ecode.A2S("32303134303530"));
		log.info(Ecode.binaryString2hexString("0000001000001001"));
		
		
		log.info(0x01^0x02^0x0^0x0a^0x01^0x52^0x13^0x60^0x39^0x61^0x98^0x46^0x30^0x30^0x30^0x30^0x30^0x30^0x30^0x30^0x37^0x34);
		String hexString = "3ABCD";  
	    log.info(hexString2binaryString(hexString));  
	    
	    String bString = "10101011110011010101";  
        log.info(binaryString2hexString(bString));  
        String b1="11010";
        log.info(binToDec(b1));
        
		
		log.info(Ecode.HEX2DEC("012A"));
		
		log.info(Ecode.DEC2HEX(-128));
		String s=Ecode.DEC2HEX(3);
		while(s.length()<4)
			s="0"+s;
		log.info(Ecode.HexString2ByteArray(s)[0]+"  "+Ecode.HexString2ByteArray(s)[1]);
		byte[] bs={0x02,0x1a,0x03,0x56,0x1f,0x3d,0x36,0x31,0x37,0x36,};
		byte[] bs2={3,-1,18};
		log.info(Ecode.ByteArray2HexString(bs, 0, 3));
		log.info(Ecode.ByteArray2HexString(bs2,-1,-1));
		
		log.info(Byte16ArrayDecString(bs,0,3));
		
		log.info((-1)&0xff);
		byte[] b=Ecode.HexString2ByteArray("8001");
		log.info(b[0]+"   "+(b[1]));
		
		
		b=createOrderID_old();
		log.info((b[0])+"   "+(b[1]));
		log.info(Ecode.S2A("请求退出营运"));//c7ebc7f3cdcbb3f6d3aad4cb
	}
}



