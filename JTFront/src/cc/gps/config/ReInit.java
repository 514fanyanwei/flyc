package cc.gps.config;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.util.DateUtil;

public class ReInit  extends TimerTask {
	private static final Log log = LogFactory.getLog(ReInit.class);
	@Override
	public void run() {
		try {
			/*
			String time=DateUtil.GetDateTime(); //格式为：yyyy-MM-dd HH:mm:ss
			String hour=time.substring(11,13);
			//String hour=time.substring(14,16);
			//log.info("检测时间"+time+"**********"+hour);
			 
			 */
			//if(hour.equals("01"))
			{
				Global.initDriverNO(); //初始化司机卡号
				//System.out.println("程序定时结束");
				//System.exit(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}