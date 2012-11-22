import java.io.IOException;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

public class Recv implements  Runnable
{
	private ClntSock thissock = null;
	public static String message = null;
	public Recv(ClntSock sock){
		thissock = sock;
	}
	public void run()
	{
		try { 			
			while(true) {
				synchronized (this) {
					message = thissock.RecvServ();
					System.out.println(message);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public String retStr() throws IOException{
		String ret = null;

		while ( ret == null ) {
			Recv.message = thissock.RecvServ();
			ret = Recv.message;
			Recv.message = null;
		}
			
		return ret;
	}
}