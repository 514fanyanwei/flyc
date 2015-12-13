package cc.gps.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class Utility {
	private static final Log log = LogFactory.getLog(Utility.class);
	public static String HEX2DEC(String hex)
	{
		return Integer.valueOf(hex,16).toString();
	}
	
	public static String DEC2HEX(int dec)
	{
		return Integer.toHexString(dec).toString();
	}
	
	public static int Oneints(int [] ints,int pos)
	{
		return ints[pos];
	}
	public static String Subints(int [] ints,int start,int end)
	{
		StringBuilder sb=new StringBuilder(ints.length);
		
		return "";
	}
	
	//字符串转换为16进制
	public static String stringToHexString(String strPart) {
		String hexString = "";
		int ch=0;
		for (int i = 0; i < strPart.length(); i++) {
			ch = (int) strPart.charAt(i);
		}
		String strHex = Integer.toHexString(ch);
		hexString = hexString + strHex;
		return hexString;
	}

   private static String hexString="0123456789ABCDEF";
	
   

	/**
	 * Convert int[] to hex string.这里我们可以将int转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * @param src int[] data
	 * @return hex string
    *字节数组 原样 转换为字符串
*/
	public static String oneintToHexString(int s)
	{
		StringBuilder stringBuilder = new StringBuilder("");
		int v = s & 0xFF;
		String hv = Integer.toHexString(v);
		if (hv.length() < 2) {
			stringBuilder.append(0);
		}
		stringBuilder.append(hv);
		
		return stringBuilder.toString();
	}
	
	public static String intsToHexString(int[] src){
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	
	/**
	 * * Convert hex string to int[]
	 * * @param hexString the hex string
	 * * @return int[]
	 * */
	public static int[] hexStringToints(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		int[] d = new int[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (int) (charToint(hexChars[pos]) << 4 | charToint(hexChars[pos + 1]));
		}
		return d;
	}
	/**
	 * * Convert char to int
	 * * @param c char
	 * * @return int
	*/
	private static int charToint(char c) {
		return (int) "0123456789ABCDEF".indexOf(c);
	}
	
	public static void main(String args[])
	{
		/*
		log.info(Utility.HEX2DEC("1A"));
		log.info(Utility.DEC2HEX(26));
		
		int[] bs={0x02,0x1a,0x03,0x56,0x1f,0x3d};
		
		for(int i=0;i<bs.length;i++)
		{
			log.info(Utility.Oneints(bs, i));
		}
		int[] bs1={bs[1],bs[2]};
		log.info(Utility.intsToHexString(bs1));
		log.info(Utility.oneintToHexString(bs1[0]));
	
		int a=0x1a;
		log.info(Utility.oneintToHexString(a));
		*/	
		String begin = "222.177.202.0";
        String end = "222.177.202.255";
        String userIP = "222.177.202.11";//客户端IP
        long a = getIpNum(begin);
        long b = getIpNum(end);
        long c = getIpNum(userIP);
        if(isInner(Long.valueOf(c),Long.valueOf(a),Long.valueOf(b))){
            log.info("在范围内");
        }else{
            log.info("在范围外");
        }

	}
	
	/* 判断是否是内网IP */
    public static boolean isInnerIP(String ipAddress) {
        boolean isInnerIp = false;
        long ipNum = getIpNum(ipAddress);
        /**
         * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
         * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum, aBegin, aEnd)
                || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
                || ipAddress.equals("127.0.0.1");
        return isInnerIp;
    }

    /* 获取IP数 */
    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);
        long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
        return ipNum;
    }

    public static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }
    public static boolean isInner(String UserIp, String Begin, String End) {
    	long begin = getIpNum(Begin);
        long end = getIpNum(End);
        long userIp = getIpNum(UserIp);
        return (userIp >= begin) && (userIp <= end);
    }
    public static Integer sendCount = 0;
    public static Integer getSendCount(){
    	sendCount++;
    	if(sendCount>=65535){
    		sendCount=0;
    	}
    	return sendCount;
    }
}



