package hex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class GustafBot2 extends Player{
	
	Random random;

	public GustafBot2(int id, int size, Color c) {
		super(id, size, c);
		random = new Random();
		
	}

	//id, res, x, y, targetX, targetY
	@Override
	public int[] algo(HashSet<Hexagon> h) {
		
		if(h.isEmpty())
			return null;
		
		ArrayList<Hexagon>  rand  =  new ArrayList<Hexagon>(), set =  new ArrayList<Hexagon>();
		HashMap<Hexagon,Integer> danger = new HashMap<Hexagon,Integer>(), free = new HashMap<Hexagon,Integer>();
		
		int[] moves = null;
		
		for(Hexagon a : h) {
			System.out.println(a);

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
		
		for(Hexagon a:h) {
			if(!rand.contains(a))
				set.add(a);
		}
		
		Collections.shuffle(rand);
		
		for(Hexagon r : rand) {
			for(Hexagon n: r.getNeighbours()) {
				if(n.getOwner() == 0) {
					free.put(n, 0);
				}else if(n.getOwner() != getId()) {
					danger.put(n, 0);
				}
			}
		}
	
		for(Hexagon e: danger.keySet()) {
			int score = 0;
			
			
			for(Hexagon ne : e.getNeighbours()) {
				if(danger.containsKey(ne))
					score++;
				
			}
			danger.put(e, danger.get(e) + score);

		}
		
		for(Hexagon f: free.keySet()) {
			int score = 0;
			for(Hexagon nf : f.getNeighbours()) {
				if(free.containsKey(nf)) 
					score++;
				
				if(danger.containsKey(nf)) 
					score--;
				
			}
			free.put(f, free.get(f) + score);
		}
		
		
		Map.Entry<Hexagon, Integer> maxdanger = null, maxfree = null;
		for(Map.Entry<Hexagon, Integer> e: danger.entrySet()) {
			//System.out.println("Danger: " + e.getKey() + " " + e.getValue());
			if(maxdanger == null || e.getValue().compareTo(maxdanger.getValue()) > 0) {
				maxdanger = e;
			}
		}
		for(Map.Entry<Hexagon, Integer> e: free.entrySet()) {
			//System.out.println("Free: " + e.getKey() + " " + e.getValue());

			if(maxfree == null || e.getValue().compareTo(maxfree.getValue()) > 0) {
				maxfree = e;
			}
		}
		
		if(maxfree == null || (maxdanger != null && maxdanger.getValue().compareTo(maxfree.getValue()) > 0)) {
			for(Hexagon n: maxdanger.getKey().getNeighbours()) {
				if(rand.contains(n)) {
					if(n.getResources() + 1 > maxdanger.getKey().getResources()) {
						moves = new int[]{getId(), n.getResources() - 1, n.getX(), n.getY(), maxdanger.getKey().getX(), maxdanger.getKey().getY()};
						break;
					}else {
						for(Hexagon s: set) {
							moves = new int[] {this.getId(), s.getResources() - 1, s.getX(), s.getY(), n.getX(), n.getY()};
							break;
						}
					}
					
				}
			}
		}else {
			Hexagon highest = null;
			for(Hexagon n: maxfree.getKey().getNeighbours()) {
				if(rand.contains(n)) {
					if(highest == null || n.getResources() > highest.getResources())
						highest = n;
				}
			}
			moves = new int[]{getId(), highest.getResources() - 1, highest.getX(), highest.getY(), maxfree.getKey().getX(), maxfree.getKey().getY()};

		}
		
		return moves;
		
	}
	
	
	/*
	 * 		for(Hexagon r : rand) {
			
				for(Hexagon n: r.getNeighbours()) {
					if(n.getOwner() == 0) {
						moves = new int[] {this.getId(), (int) (0.8*r.getResources()), r.getX(), r.getY(), n.getX(), n.getY()};
						break;
					}else if(n.getOwner() != getId()) {
						if(n.getResources() + 1 >= r.getResources()) {
							for(Hexagon s: h) {
								if(!rand.contains(s)) {
									moves = new int[] {this.getId(), s.getResources() - 1, s.getX(), s.getY(), r.getX(), r.getY()};
									break;
								}
							}
						}else {
							moves = new int[] {this.getId(), n.getResources() + 1, r.getX(), r.getY(), n.getX(), n.getY()};
							break;
						}
					}
				}
			
			}
	 */

	
}
