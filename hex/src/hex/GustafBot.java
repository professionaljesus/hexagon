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


	}
	
	
	//id, res, x, y, targetX, targetY
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
		
		for(Hexagon r : rand) {
			if(moves == null) {
				for(Hexagon n: r.getNeighbours(H)) {
					if(n.getOwner() == 0) {
						moves = new int[] {getId(), 6, (3*r.getResources() )/ 4, r.getX(), r.getY(), n.getX(), n.getY()};
					}
				}



				if(r.getResources() < 70) {
					for(Hexagon s: set) {
						if(!rand.contains(s)) {
							moves = new int[] {getId(), 6, (3*s.getResources() )/ 4, s.getX(), s.getY(), r.getX(), r.getY()};
						}

					}
				}
			}
		}
		
		Hexagon m = itr.next();
		moves = new int[] {1, 5, 0, 5, 0, 6};
		 
		
		return moves;
		
	}

	
}
