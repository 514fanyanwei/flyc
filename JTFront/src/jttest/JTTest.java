package jttest;


import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

import cc.gps.config.Config;
import cc.gps.util.Ecode;

public class JTTest {
	public static void main(String args[]) {
		int num=10;
		Thread[] thread=new Thread[num];
		for(int i=0;i<num;i++){
			String sim1="01310000";
			String sim2=String.valueOf(i);
			while(sim2.length()<4){
				sim2="0"+sim2;
			}
			sim1=sim1+sim2;
			thread[i]=new CreateThread(String.valueOf(i),sim1);
			thread[i].start();
			
			try{
			if(i%500==0) 
				Thread.sleep(1000);
			}catch(Exception e){}
		}
		
  }
}
class CreateThread extends Thread{
	String name="";
	String sim="";
	Socket socket=null;
	CreateThread(String name,String sim){
		this.name=name;
		this.sim=sim;
	}
	public void run(){
		int n=0;
		while(true)
		{
			n++;
			if(n>3) break;
		try{
        	System.out.println("create "+name+" connection");
			socket=new Socket("127.0.0.1",38880);
			
			//socket=new Socket("192.168.3.4",38880);
			//sleep(1000*10);
			send();
			//break;
	      }catch(Exception e) {
	        System.out.print("Error :"+name+"  "+ e); //出错，则打印出错信息
	        try{
	        sleep(1000*3);
	        }catch(Exception e1){
	        	System.out.print("REPEATE Error :"+name+"  "+ e1); //出错，则打印出错信息
	        }
	        
	      }
		}
	}
	public void send(){
		System.out.println(name+"   "+"start send***********");
		try{
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			//18996408175
			//18996408175
			int len=2;
			String str[]=new String[len];
			str[0]="7e0102000a"+sim+"04cd30303030303030303231b97e";//0102
			
			str[1]="7e02000030"+sim+"023c000000000000000301c292700658d1800151000000001407120815560104000fad6803020000e1080000000000000000697e";//0200
			
			byte[][]ps=new byte[len][];
			ps[0]=Ecode.HexString2ByteArray(str[0]);
			out.write(ps[0]);
			//while(true){
			int count=10;
			for(int i=0;i<count;i++)
			{
				ps[1]=Ecode.HexString2ByteArray(str[1]);
				sleep(10000);
				out.write(ps[1]);
				//sleep(1000);
			}
			
			System.out.println("receive");
			byte[] rp=new byte[200];
			
			in.read(rp);
			if (rp!=null)
			for(int i=0;i<rp.length;i++)
			{
				System.out.print(rp[i]+"  |   ");
			}
			

	        socket.close(); //关闭Socket
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}

