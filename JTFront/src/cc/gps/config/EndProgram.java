package cc.gps.config;

import java.io.RandomAccessFile;
import java.util.TimerTask;

import cc.gps.util.DateUtil;
//没有使用
public class EndProgram extends TimerTask {
	
	@Override
	public void run() {
		try {
			String time=DateUtil.GetDateTime(); //格式为：yyyy-MM-dd HH:mm:ss
			//String hour=time.substring(11,13);
			String hour=time.substring(14,16);
			System.out.println("结束检测"+time+"**********"+hour);
			if(hour.equals("04")){
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