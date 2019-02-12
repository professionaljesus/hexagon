package hex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class GustafBot extends Player{
	
	ArrayList<Hexagon> rand;
	Random random;

	public GustafBot(int id, int size, Color c) {
		super(id, size, c);
		random = new Random();

	}

	//id, res, x, y, targetX, targetY
	@Override
	public int[] algo(HashSet<Hexagon> h) {
		if(h.isEmpty())
			return null;
		
		rand =  new ArrayList<Hexagon>();
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
		
		Collections.shuffle(rand);
		

		for(Hexagon r : rand) {
			
			
			for(Hexagon n: r.getNeighbours()) {
				if(n.getOwner() == 0) {
					return moves = new int[] {this.getId(), r.getResources() - 1, r.getX(), r.getY(), n.getX(), n.getY()};
				
				}else if(n.getOwner() != getId()) {
					if(n.getResources() + 1 >= r.getResources()) {
						for(Hexagon s: h) {
							if(!rand.contains(s)) {
								return new int[] {this.getId(), s.getResources() - 1, s.getX(), s.getY(), r.getX(), r.getY()};
								
							}
						}
					}else {
						return moves = new int[] {this.getId(), n.getResources() + 1, r.getX(), r.getY(), n.getX(), n.getY()};
						
					}
				}
			}
			
				
			
		}
				

		return null;
		
	}

	
}
