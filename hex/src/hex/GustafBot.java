package hex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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
	
	/**
	 * Get's called at the endofTurn t[0] user 
	 * t[1] direction 
	 * t[2] resource 
	 * t[3] x
	 * t[4] y t[5] targetX t[6] targetY
	 * 
	 * Om dir > 5 så går den på targetX och Y
	 */

	@Override
	public int[] algo(HashSet<Hexagon> h) {
		int[] moves = null;
		Iterator<Hexagon> itr = h.iterator();
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
		
		Hexagon m = itr.next();
		moves = new int[] {1, 1, 1, m.getX(), m.getY()};
		 
		
		return moves;
		
	}

	
}
