/*
 * 创建日期 2007-03-14
 *
 * 功能  取日期时间工具
 *
 */

package cc.gps.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.net.jt808.JT808FrontServer;


public  class DateUtil {

 public DateUtil(){}
 /**
     * @see     取得当前日期（格式为：yyyy-MM-dd）
     * @return String
     */
 private static final Log log = LogFactory.getLog(DateUtil.class);
    public static String GetDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(new Date());
        return sDate;
    }
   
    /**
     * @see     取得当前时间（格式为：yyyy-MM-dd HH:mm:ss）
     * @return String
     */
    public static String GetDateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDate = sdf.format(new Date());
        return sDate;
    }

    /**
     * @see     按指定格式取得当前时间()
     * @return String
     */
    public static String GetTimeFormat(String strFormat)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
        String sDate = sdf.format(new Date());
        return sDate;
    }

    /**
     * @see     取得指定时间的给定格式()
     * @return String
     * @throws ParseException
     */
    public static String SetDateFormat(String myDate,String strFormat) throws ParseException
    {

        SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
        String sDate = sdf.format(sdf.parse(myDate));

        return sDate;
    }

    public static String FormatDateTime(String strDateTime, String strFormat)
    {
        String sDateTime = strDateTime;
        try {
            Calendar Cal = parseDateTime(strDateTime);
            SimpleDateFormat sdf = null;
            sdf = new SimpleDateFormat(strFormat);
            sDateTime = sdf.format(Cal.getTime());
        } catch (Exception e) {

        }
        return sDateTime;
    }

    public static Calendar parseDateTime(String baseDate)
    {
        Calendar cal = null;
        cal = new GregorianCalendar();
        
        int yy = Integer.parseInt(baseDate.substring(0, 4));
        int mm = Integer.parseInt(baseDate.substring(5, 7)) - 1;
        int dd = Integer.parseInt(baseDate.substring(8, 10));
        int hh = 0;
        int mi = 0;
        int ss = 0;
        if(baseDate.length() > 12)
        {
            hh = Integer.parseInt(baseDate.substring(11, 13));
            mi = Integer.parseInt(baseDate.substring(14, 16));
            ss = Integer.parseInt(baseDate.substring(17, 19));
        }
        cal.set(yy, mm, dd, hh, mi, ss);
        return cal;
    }
    public static int getDay(String strDate)
    {
        Calendar cal = parseDateTime(strDate);
        return  cal.get(Calendar.DATE);
    }

    public static int getMonth(String strDate)
    {
        Calendar cal = parseDateTime(strDate);

        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getWeekDay(String strDate)
    {
        Calendar cal = parseDateTime(strDate);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String getWeekDayName(String strDate)
    {
        String mName[] = {
            "日", "一", "二", "三", "四", "五", "六"
        };
        int iWeek = getWeekDay(strDate);
        iWeek = iWeek - 1;
        return "星期" + mName[iWeek];
    }

    public static int getYear(String strDate)
    {
        Calendar cal = parseDateTime(strDate);
        return cal.get(Calendar.YEAR) + 1900;
    }

    public static String DateAdd(String strDate, int iCount, int iType)
    {
        Calendar Cal = parseDateTime(strDate);
        int pType = 0;
        if(iType == 0)
        {
            pType = 1;
        } else
        if(iType == 1)
        {
            pType = 2;
        } else
        if(iType == 2)
        {
            pType = 5;
        } else
        if(iType == 3)
        {
            pType = 10;
        } else
        if(iType == 4)
        {
            pType = 12;
        } else
        if(iType == 5)
        {
            pType = 13;
        }
        Cal.add(pType, iCount);
        SimpleDateFormat sdf = null;
        if(iType <= 2)
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        else
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDate = sdf.format(Cal.getTime());
        return sDate;
    }

    public static String DateAdd(String strOption, int iDays, String strStartDate)
    {
        if(!strOption.equals("d"));
        return strStartDate;
    }

    public static int DateDiff(String strDateBegin, String strDateEnd, int iType)
    {
        
    	try{
        	Calendar calBegin = parseDateTime(strDateBegin);
        	Calendar calEnd = parseDateTime(strDateEnd);
        	long lBegin = calBegin.getTimeInMillis();
            long lEnd = calEnd.getTimeInMillis();
            int ss = (int)((lBegin - lEnd) / 1000L);
            int min = ss / 60;
            int hour = min / 60;
            int day = hour / 24;
            if(iType==3)
            	return ss;
            if(iType == 0)
                return hour;
            if(iType == 1)
                return min;
            if(iType == 2)
                return day;
        }catch(Exception e){
        	e.printStackTrace();
        }
        return -1;
        
    }
    
    /*****************************************
     * @功能     判断某年是否为闰年
     * @return  boolean
     * @throws ParseException
     ****************************************/
    public static boolean isLeapYear(int yearNum){
     boolean isLeep = false;
        /**判断是否为闰年，赋值给一标识符flag*/
        if((yearNum % 4 == 0) && (yearNum % 100 != 0)){
         isLeep = true;
        }  else if(yearNum % 400 ==0){
         isLeep = true;
        } else {
         isLeep = false;
        }
        return isLeep;
    }

    
     /*****************************************
     * @功能     计算当前日期某年的第几周
     * @return  interger
     * @throws ParseException
     ****************************************/
    public static int getWeekNumOfYear(){
     Calendar calendar = Calendar.getInstance();
     int iWeekNum = calendar.get(Calendar.WEEK_OF_YEAR);
     return iWeekNum;
    }

    /*****************************************
     * @功能     计算指定日期某年的第几周
     * @return  interger
     * @throws ParseException
     ****************************************/
    public static int getWeekNumOfYearDay(String strDate ) throws ParseException{
     Calendar calendar = Calendar.getInstance();
     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
     Date curDate = format.parse(strDate);
     calendar.setTime(curDate);
     int iWeekNum = calendar.get(Calendar.WEEK_OF_YEAR);
     return iWeekNum;
    }
    /*****************************************
     * @功能     计算某年某周的开始日期
     * @return  interger
     * @throws ParseException
     ****************************************/
    public static  String getYearWeekFirstDay(int yearNum,int weekNum) throws ParseException {
     
     Calendar cal = Calendar.getInstance();
     cal.set(Calendar.YEAR, yearNum);
     cal.set(Calendar.WEEK_OF_YEAR, weekNum);
     cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
     //分别取得当前日期的年、月、日
     String tempYear = Integer.toString(yearNum);
     String tempMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
     String tempDay = Integer.toString(cal.get(Calendar.DATE));
     String tempDate = tempYear + "-" +tempMonth + "-" + tempDay;
     return SetDateFormat(tempDate,"yyyy-MM-dd");


     
    }
    /*****************************************
     * @功能     计算某年某周的结束日期
     * @return  interger
     * @throws ParseException
     ****************************************/
    public static String getYearWeekEndDay(int yearNum,int weekNum) throws ParseException {
     Calendar cal = Calendar.getInstance();
     cal.set(Calendar.YEAR, yearNum);
     cal.set(Calendar.WEEK_OF_YEAR, weekNum + 1);
     cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //分别取得当前日期的年、月、日
     String tempYear = Integer.toString(yearNum);
     String tempMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
     String tempDay = Integer.toString(cal.get(Calendar.DATE));
     String tempDate = tempYear + "-" +tempMonth + "-" + tempDay;
     return SetDateFormat(tempDate,"yyyy-MM-dd");
    }
    
    
    /*****************************************
     * @功能     计算某年某月的开始日期
     * @return  interger
     * @throws ParseException
     ****************************************/
    public static String getYearMonthFirstDay(int yearNum,int monthNum) throws ParseException {
     
     //分别取得当前日期的年、月、日
     String tempYear = Integer.toString(yearNum);
     String tempMonth = Integer.toString(monthNum);
     String tempDay = "1";
     String tempDate = tempYear + "-" +tempMonth + "-" + tempDay;
     return SetDateFormat(tempDate,"yyyy-MM-dd");
     
    }
    /*****************************************
     * @功能     计算某年某月的结束日期
     * @return  interger
     * @throws ParseException
     ****************************************/
    public static String getYearMonthEndDay(int yearNum,int monthNum) throws ParseException {
     

       //分别取得当前日期的年、月、日
     String tempYear = Integer.toString(yearNum);
     String tempMonth = Integer.toString(monthNum);
     String tempDay = "31";
     if (tempMonth.equals("1") || tempMonth.equals("3") || tempMonth.equals("5") || tempMonth.equals("7") ||tempMonth.equals("8") || tempMonth.equals("10") ||tempMonth.equals("12")) {
      tempDay = "31";
     }
     if (tempMonth.equals("4") || tempMonth.equals("6") || tempMonth.equals("9")||tempMonth.equals("11")) {
      tempDay = "30";
     }
     if (tempMonth.equals("2")) {
      if (isLeapYear(yearNum)) {
       tempDay = "29";
      } else {
         tempDay = "28";
      }
     }
     //log.info("tempDay:" + tempDay);
     String tempDate = tempYear + "-" +tempMonth + "-" + tempDay;
     return SetDateFormat(tempDate,"yyyy-MM-dd");

    }
    
    public static void main(String args[]){
    	String s=GetDateTime();
    	log.info(s);
    	log.info(DateDiff("2014-03-20 04:02:48","2014-03-19 04:02:48",3));//2013-11-21 13:45:50   202013-11-21 13:45:11
    	String s1="2014-03-20 04:02:50";
    	String s2="2014-03-20 04:02:49";
    	System.out.println(s1.compareTo(s2));
    	System.out.println(GetTimeFormat("yy-mm-dd HH:mm:ss"));
    	System.out.println(s.substring(2,4)+s.substring(5,7)+s.substring(8,10)+s.substring(11,13)+s.substring(14,16)+s.substring(17,19));
    }

}