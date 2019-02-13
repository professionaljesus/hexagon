package hex;

import java.awt.Color;
import java.util.HashSet;

public class BeppeBot extends Player{
	
	public Hexagon[][] map;

	public BeppeBot(int id, int size, Color c) {
		super(id, size, c);
		map = new Hexagon[size*2-1][size*2-1];
	}

	
	//id, res, x, y, targetX, targetY
	@Override
	public int[] algo(HashSet<Hexagon> H) {
		for(Hexagon hex: H){
			map[hex.getX()][hex.getY()] = hex;
		}
		
		
		
		return null;
	}

}
