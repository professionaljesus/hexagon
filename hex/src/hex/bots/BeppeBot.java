package hex.bots;

import java.awt.Color;
import java.util.HashSet;

import hex.Hexagon;
import hex.Player;

public class BeppeBot extends Player{
	
	public Hexagon[][] map;

	public BeppeBot(int id, int size, Color c, String name) {
		super(id, size, c, name);
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
