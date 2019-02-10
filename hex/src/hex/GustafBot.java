package hex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class GustafBot extends Player{
	
	ArrayList<Hexagon> rand;
	ArrayList<Hexagon> set;
	ArrayList<Hexagon> rbs;

	public GustafBot(int id, int size, Color c) {
		super(id, size, c);
		rand = new ArrayList<Hexagon>();
		set = new ArrayList<Hexagon>();
		rbs = new ArrayList<Hexagon>();


	}

	@Override
	public int[] algo(HashSet<Hexagon> h) {
		int[] moves = null;
		for(Hexagon a : h) {
			boolean r = false;

			for(Hexagon n : a.getNeighbours()) {
				if(n.getOwner() != getId()) {
					r = true;
					break;
				}
			}
			if(r)
				rand.add(a);
		}
		
		
			
		 
		
		return moves;
		
	}

	
}
