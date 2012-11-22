import java.util.ArrayList;

public class Wall {
	ArrayList<Missile> arrM;
	
	public Wall() {
		arrM = new ArrayList<Missile>();
	}
	
	public void addMissile(Coords c) {
		if (arrM.size() <= Config.getWallLimit())
			arrM.add(new Missile(c));
	}
}
