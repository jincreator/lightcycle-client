import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;
import java.io.IOException;

public class Game extends JFrame implements Runnable, KeyListener{
	//변수 선언부
	private int w = 1000, h = 800;
	private ClntSock mysock = null;
	private String str = null;
	private String name;
	private BufferedImage bi = null;
	private Cycle mycycle = null;
	private Cycle enemycycle = null;
	private boolean ready_player = false, ready_enemy = false;
	private boolean player = true;
	private boolean left = false, right = false, up = false, down = false, drawWall = false;
	private boolean start = false;
	
	//생성자
	public Game() throws IOException{
		bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		JOptionPane.showMessageDialog(null, "ENTER THE GAME!!!");
		mysock = new ClntSock(Config.getIP(),Config.getPort());
		name = JOptionPane.showInputDialog("ENTER YOUR NAME");
		mysock.SendServ("HELO "+name);
		String temp = mysock.RecvServ();
		if(temp.trim().charAt(0)==name.charAt(0)){
			player = true;
		}else{
			player = false;
		}
		mycycle = new Cycle(player);
		enemycycle = new Cycle(!player);
		this.addKeyListener(this);
 		this.setSize(w, h);
 		this.setTitle("TEAM PROJECT");
 		this.setResizable(false);
 		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		this.setVisible(true);  
	}
	
	public void run() {
 		try {
 			int playerCnt = 0;
 			int enemyCnt = 0;
 			while(true) {
 				Thread.sleep(50);
 				if(ready_player){
 					mysock.SendServ("READY");
 					String msgrecv=mysock.RecvServ();
 					if(msgrecv.charAt(0)=='S'&& msgrecv.charAt(4)=='T'){
 						start = true;
 					}
 				}
 				if(start){
 					fire();
 					keyControl();
 					control();
 					crashChk();
 				}
 				draw();
 			}
 		} catch(Exception e) {
 			e.printStackTrace();
		}
 	}
	
	public void fire(){
		int x = mycycle.getX() + (Config.getCycleSize()/2);
		int y = mycycle.getY() + (Config.getCycleSize()/2);
		Coords temp = new Coords(x,y);
		if(drawWall)
			mycycle.getLine().addMissile(temp);
	}
	
	public void crashChk(){
		Graphics g = this.getGraphics();
 		Polygon p = null;
 		
 		for(int i = 0; i < mycycle.getLine().arrM.size(); i++){
 			Missile m = (Missile)mycycle.getLine().arrM.get(i);
 			int[] xpoints = {m.getCoords().getX(), (m.getCoords().getX() + Config.getMissileSize()), (m.getCoords().getX() + Config.getMissileSize()), m.getCoords().getX()};
 			int[] ypoints = {m.getCoords().getY(), m.getCoords().getY(), (m.getCoords().getY() + Config.getMissileSize()), (m.getCoords().getY() + Config.getMissileSize())};
 			p = new Polygon(xpoints, ypoints, 4);
 			if(p.intersects((double)enemycycle.getX(),(double)enemycycle.getY(), (double)Config.getCycleSize(),(double)Config.getCycleSize()))
 			{
 				start = false;
 				mysock.SendServ("WIN "+name);
 			}
 		}
 		for(int i = 0; i < enemycycle.getLine().arrM.size(); i++){
 			Missile enm = (Missile)enemycycle.getLine().arrM.get(i);
 			int[] xpoints = {enm.getCoords().getX(), (enm.getCoords().getX() + Config.getMissileSize()), (enm.getCoords().getX() + Config.getMissileSize()), enm.getCoords().getX()};
 			int[] ypoints = {enm.getCoords().getY(), enm.getCoords().getY(), (enm.getCoords().getY() + Config.getMissileSize()), (enm.getCoords().getY() + Config.getMissileSize())};
 			p = new Polygon(xpoints, ypoints, 4);
 			if(p.intersects((double)mycycle.getX(),(double)mycycle.getY(), (double)Config.getCycleSize(),(double)Config.getCycleSize()))
 			{
 				start = false;
 			}
 		}
	}
	
	public void control() throws IOException{
		int enx = enemycycle.getX();
		int eny = enemycycle.getY();
		int size = Config.getCycleSize();
		Coords temp = new Coords(enx, eny);
		str = mysock.RecvServ();
		System.out.println("str us " + str);
		if(enx>0){
			if(str.charAt(5)=='1')
				enemycycle.addX(-5);
		}
		if(enx + size < w){
			if(str.charAt(6)=='1')
				enemycycle.addX(5);
		}
		if(eny > 25){
			if(str.charAt(7)=='1')
				enemycycle.addY(-5);
		}
		if(eny + size < h){
			if(str.charAt(8)=='1')
				enemycycle.addY(5);
		}
		if(str.charAt(9)=='1'){
			enemycycle.getLine().addMissile(temp);
		}else{
			int i = 0;
			while(enemycycle.getLine().arrM.size()!=0){
				enemycycle.getLine().arrM.remove(i);
				i++;
				if(i>=enemycycle.getLine().arrM.size()) i=0;
			}
		}
	}
		
	public void draw() {
		int x = mycycle.getX();
		int y = mycycle.getY();
		int enx = enemycycle.getX();
		int eny = enemycycle.getY();
		int size = Config.getCycleSize();
		int msSize = Config.getMissileSize();
 		
		Graphics gs = bi.getGraphics();
 		gs.setColor(Color.black);
 		gs.fillRect(0, 0, w, h);
 		gs.setColor(Color.white);
 		if(!start)
 			gs.drawString("게임 시작", 500, 400);
 		if(player){
 			gs.setColor(Color.white);
 		}else{
 			gs.setColor(Color.yellow);
 		}
 		gs.fillRect(x, y, size, size);
 		
 		for(int i = 0;  i < mycycle.getLine().arrM.size(); i++){
 			Missile m = (Missile)mycycle.getLine().arrM.get(i);
 			gs.fillRect(m.getCoords().getX(), m.getCoords().getY(), msSize, msSize);
 		}
 		
 		if(player){
 			gs.setColor(Color.yellow);
 		}else{
 			gs.setColor(Color.white);
 		}
 		
 		gs.fillRect(enx, eny, size, size);
 		for(int i = 0;  i < enemycycle.getLine().arrM.size(); i++){
 			Missile m = (Missile)enemycycle.getLine().arrM.get(i);
 			gs.fillRect(m.getCoords().getX(), m.getCoords().getY(), msSize, msSize);
 		} 		
 		Graphics ge = this.getGraphics();
 		ge.drawImage(bi, 0, 0, w, h, this);
 	}
	
	public void keyControl()
	{
		int x = mycycle.getX();
		int y = mycycle.getY();
		int size = Config.getCycleSize();
		String message = "NONE";
		
		if(drawWall){
			if(x>0) {
				if(left){
					mycycle.addX(-5);
					message = "MGHG 10001";
				}
			}
			if(x + size < w){
				if(right){
					mycycle.addX(5);
					message = "MGHG 01001";
				}
			}
			if(y > 25){
				if(up){
					mycycle.addY(-5);
					message = "MGHG 00101";
				}
			}
			if(y + size < h){
				if(down){
					mycycle.addY(5);
					message = "MGHG 00011";
				}
			}
		}else{
			if(x>0) {
				if(left){
					mycycle.addX(-5);
					message = "MGHG 10000";
				}
			}
			if(x + size < w){
				if(right){
					mycycle.addX(5);
					message = "MGHG 01000";
				}
			}
			if(y > 25){
				if(up){
					mycycle.addY(-5);
					message = "MGHG 00100";
				}
			}
			if(y + size < h){
				if(down){
					mycycle.addY(5);
					message = "MGHG 00010";
				}
			}
		}
		
		mysock.SendServ(message);
	}
		
	public void keyPressed(KeyEvent ke) {
 		switch(ke.getKeyCode()) {
 			case KeyEvent.VK_LEFT:
				left = true;
				right = false;
				up = false;
				down = false;
				break;
			case KeyEvent.VK_RIGHT:
				right = true;
				left = false;
				up = false;
				down = false;
				break;
			case KeyEvent.VK_UP:
				up = true;
				right = false;
				left = false;
				down = false;
				break;
			case KeyEvent.VK_DOWN:
				down = true;
				left = false;
				right = false;
				up = false;
				break;
 			case KeyEvent.VK_ENTER:
				ready_player = true;
				break;
 			case KeyEvent.VK_SPACE:
 				drawWall = true;
				break;
 		}
 	}
	
 	public void keyReleased(KeyEvent ke) {
 		switch(ke.getKeyCode()) {
 			case KeyEvent.VK_SPACE:
 				drawWall = false;
 				int i = 0;
 				while(mycycle.getLine().arrM.size()!=0){
 					mycycle.getLine().arrM.remove(i);
 					i++;
 					if(i>=mycycle.getLine().arrM.size()) i=0;
 				}
 				break;
 		}
 	}
 
 	public void keyTyped(KeyEvent ke) {}
 	
 	public static void main(String[] args) throws IOException {
 		Thread t = new Thread(new Game());
 		t.start();
 	}
}
