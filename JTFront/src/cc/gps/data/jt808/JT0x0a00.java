package cc.gps.data.jt808;

public class JT0x0a00  extends JTReceiveData{
	/**
	 * 
	 */
	private static final long serialVersionUID = 20017L;
	public long rsa_e; //终端 RSA 公钥{e,n}中的 e
	public int[] rsa_n=new int[128]; //RSA 公钥{e,n}中的 n
}
