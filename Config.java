public class Config {
	public static String ret = null;
	private static String IP = "165.194.35.4";
	private static int port = 9999;
	private static int cyclesize = 24;
	private static int missileSize = 5;
	private static int startingTopX = 500;
	private static int startingTopY = 100;
	private static int startingBottomX = 500;
	private static int startingBottomY = 700;
	
	private static int walllimit = 500;
	private static int walltimer = 10000;
	
	public static Coords getStartingTop() {
		return new Coords(startingTopX, startingTopY);
	}
	
	public static Coords getStartingBottom() {
		return new Coords(startingBottomX, startingBottomY);
	}
	
	public static String getIP(){
		return IP;
	}
	
	public static int getPort(){
		return port;
	}
	
	public static int getMissileSize(){
		return missileSize;
	}
	
	public static int getCycleSize(){
		return cyclesize;
	}
	
	public static int getWallLimit() {
		return walllimit;
	}
	
	public static int getWallTimer() {
		return walltimer;
	}
}
