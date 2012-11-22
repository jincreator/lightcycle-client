
public class Cycle {
	private Wall line = new Wall();
	private Coords crd;
	private boolean color;
	
	public Cycle(boolean color) {
		this.color = color;
		if (color) { //Starts at bottom
			crd = Config.getStartingBottom();
		} else {
			crd = Config.getStartingTop();
		}
	}
	public Wall getLine(){
		return line;
	}
	public int getX() {
		return crd.getX();
	}
	public int getY() {
		return crd.getY();
	}
	public void addX(int a) {
		crd.setX(crd.getX()+a);
	}
	public void addY(int a) {
		crd.setY(crd.getY()+a);
	}
}
