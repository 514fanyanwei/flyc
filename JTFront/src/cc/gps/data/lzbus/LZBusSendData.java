package cc.gps.data.lzbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cc.gps.data.SendData;
import cc.gps.data.jt808.JTMessageHead;
import cc.gps.util.Ecode;

public class LZBusSendData extends SendData{
	
	public String start="02";
	public String body;        //待下发终端的数据
	public int result=0;  //终端处理结果  0:成功  1:失败 2:消息有误  3:不支持???

	//public JTReceiveData transData;//将接收的数据转发到平台的数据
	private static final Log log = LogFactory.getLog(LZBusSendData.class);
	/*
	 * 
	 * 1 16进制字符串每2个一组转10进制,后组成字节数组
	 * 2  组装开始、结束标识位
	 */
	public byte[] toByte()   //将下发终端的数据生成byte数组
	{
		
		//log.info(body+"   *"+body.length());
		byte []bs=new byte[body.length()/2+1 +2+1];  //1 开始位   2 留给数据长度    1结束位 
		//16进制字符串每2个一组转10进制,后组成字节数组
		//"FFIA"--->-1 ,26 即 每2个字符一组 作为16进制看 0xFF --->-1  01A--->26
		byte[] temp=Ecode.HexString2ByteArray(body);
		int len=temp.length;
		
		for(int i=0;i<len;i++){
			bs[i+3]=temp[i];
		}
		bs[0]=(byte)0x02;
		
		String slen=Ecode.DEC2HEX(len);
		//log.info(slen);
		while(slen.length()<4){
			slen="0"+slen;
		}
		byte[] tlen=Ecode.HexString2ByteArray(slen);
		bs[1]=tlen[0];
		bs[2]=tlen[1];
		
		bs[len+3]=(byte)0x03;
		/*
		if(log.isDebugEnabled()){
			log.info("发向终端的内容：");
			for(int i=0;i<bs.length;i++){
				log.info((byte)bs[i]+ " * ");
			}
		}*/
		
		
		return bs;

	}
	

}