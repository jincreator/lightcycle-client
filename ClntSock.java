import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class ClntSock {
	static Socket sock;
	static PrintWriter pw;

	private InputStream inputStream;

	private byte[] buf = new byte[1024];
	
	public ClntSock(String IP, int port) throws UnknownHostException, IOException{
		sock= new Socket(IP, port);
		pw = new PrintWriter(sock.getOutputStream());
		inputStream = sock.getInputStream();
	}
	
	public void SendServ(String message){
		pw.print(message);
		pw.flush();
	}
	
	public String RecvServ() throws IOException{
		inputStream.read(buf);
		
		return new String(buf);
	}
	
	public void finalize() throws IOException{
		pw.close();
		sock.close();
	}
}
