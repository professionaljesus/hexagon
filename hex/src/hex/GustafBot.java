package hex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class GustafBot extends Player{
	
	ArrayList<Hexagon> rand;
	ArrayList<Hexagon> set;

	public GustafBot(int id, int size, Color c) {
		super(id, size, c);
		rand = new ArrayList<Hexagon>();
		set = new ArrayList<Hexagon>();

	}

	@Override
	public int[] algo(HashSet<Hexagon> H) {
		int[] moves = null;
	
			

		
		return moves;
		
	}

	
}
