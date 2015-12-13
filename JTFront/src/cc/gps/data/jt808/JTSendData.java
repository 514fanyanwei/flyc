package cc.gps.data.jt808;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cc.gps.data.SendData;
import cc.gps.util.Ecode;

//
public class JTSendData extends SendData{
	public String start="7e";
	//public JTMessageHead head;
	public String body="";        //待下发终端的数据
	public int checkCode;
	public static int countid=0;  //流水号
	
	public int result=0;  //终端处理结果  0:成功  1:失败 2:消息有误  3:不支持

	public JTReceiveData transData;//将接收的数据转发到平台的数据
	private static final Log log = LogFactory.getLog(JTSendData.class);
	/*
	 * 1  加入生成的流水号
	 * 2 16进制字符串每2个一组转10进制,后组成字节数组
	 * 3  组装开始、校验码、结束标识位
	 */
	public byte[] toByte()   //将下发终端的数据生成byte数组
	{
		/*if(countid>65535) countid=0;
		countid++;
		head.serialID=countid;*/
		//流水号放到Web端生成
		//String str=start+head.toStrings()+body;
		String str=head.toStrings()+body;
		//log.info("JTSendData.toByte()***:"+str);
		byte [] bs=new byte[str.length()/2+1+1];  //1个开始位 1个校验位 1个结束标识
		//16进制字符串每2个一组转10进制,后组成字节数组
		//"FFIA"--->-1 ,26 即 每2个字符一组 作为16进制看 0xFF --->-1  01A--->26
		byte[] temp=Ecode.HexString2ByteArray(str);
		int len=temp.length;
		for(int i=0;i<len;i++){
			//log.info(temp[i]+" ** ");
			bs[i]=temp[i];
		}
		//log.info();
	
		//int cc=Ecode.buildCheckCode(str.substring(2)); //开始标识位不参与校验
		int cc=Ecode.buildCheckCode(str); 
		//log.info("check code:"+cc);
		bs[len]=(byte)cc;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(0x7e);
		for(int j=0;j<bs.length-1;j++){
			if(bs[j]==0x7d){
				try {
					out.write(new byte[]{0x7d,0x01});
				} catch (IOException e) {
					e.printStackTrace();
				}
				//转义处理完成
				continue;
			}
			if(bs[j]==0x7e){
				try {
					out.write(new byte[]{0x7d,0x02});
				} catch (IOException e) {
					e.printStackTrace();
				}
				//转义处理完成
				continue;
			}
			
			{
				out.write(bs[j]);
			}
		}
		out.write(0x7e);	
		
		bs[len+1]=(byte)0x7e;

		/*
		log.info("JTSendData下发指令内容:");
		for(int i=0;i<bs.length;i++)
			log.info(bs[i]+"  ");*/
		//log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+Ecode.ByteArray2HexString(bs, 0, bs.length));
		return out.toByteArray();
		//return bs;
	}
	

}
