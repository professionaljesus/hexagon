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
	double boost;
	boolean helpedlast;

	public GustafBot2(int id, int size, Color c, String name) {
		super(id, size, c, name);
		random = new Random();
		boost = 0.8;
		
	}
	
	private int[] move(int res, Hexagon n, Hexagon e) {
		return new int[] {getId(), res, n.getX(), n.getY(), e.getX(), e.getY()};
	}
	
	private ArrayList<Hexagon> risks(ArrayList<Hexagon> a) {
		ArrayList<Hexagon> underattack = new ArrayList<Hexagon>();
		
		for(Hexagon r: a) {
			for(Hexagon n: r.getNeighbours()) {
				if(n.getOwner() != getId() && n.getOwner() != 0 && n.getResources() >= r.getResources())
					underattack.add(r);
					
			}
		}
		
		return underattack;
	}
	
	private ArrayList<Hexagon> enemies(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();

		for(Hexagon e : a.getNeighbours()) {
			if(e.getOwner() != getId())
				enemies.add(e);
		}
		return enemies;
	}


	//id, res, x, y, targetX, targetY
	@Override
	public int[] algo(HashSet<Hexagon> h) {
		
		if(h.isEmpty())
			return null;
		
		ArrayList<Hexagon> rand =  new ArrayList<Hexagon>();
		ArrayList<Hexagon> set =  new ArrayList<Hexagon>();

		
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
		
		for(Hexagon a: h) {
			if(!rand.contains(a))
				set.add(a);
		}
		
		Collections.shuffle(rand);
		
		ArrayList<Hexagon> underattack = risks(rand);
		if(underattack.size() == 0 || helpedlast) {
			helpedlast = false;
			return move(rand.get(0).getResources() - 1, rand.get(0), enemies(rand.get(0)).get(0));
		}else {
			helpedlast = true;
			Hexagon boi = Collections.min(underattack);
			Hexagon helper;
			if(set.size() != 0)
				helper = Collections.max(set);
			else
				helper = Collections.max(rand);
			
			return move(helper.getResources() - 1, helper, boi);
			
		}

		//return null;
		
	}
	
	/*
	 * if(h.isEmpty())
			return null;
		
		
		HashMap<Hexagon, ArrayList<Hexagon>> attackers = new HashMap<Hexagon, ArrayList<Hexagon>>();
		ArrayList<Hexagon>  rand  =  new ArrayList<Hexagon>(), set =  new ArrayList<Hexagon>();
		HashMap<Hexagon,Integer> danger = new HashMap<Hexagon,Integer>(), free = new HashMap<Hexagon,Integer>(), safety = new HashMap<Hexagon, Integer>();
		
		int[] moves = null;
		
		for(Hexagon a : h) {
			boolean r = false;
			for(Hexagon n : a.getNeighbours()) {
				if(n.getOwner() != getId()) {
					r = true;
					break;
				}
			}
			if(r) {
				rand.add(a);
				for(Hexagon nr : a.getNeighbours()) {
					if(nr.getOwner() != getId()) {
						if(attackers.get(nr) == null) {
							ArrayList<Hexagon> boi = new ArrayList<Hexagon>();
							boi.add(a);
							attackers.put(nr, boi);
						}else 
							attackers.get(nr).add(a);
						
					}
				}
			}
		}
		
		for(Hexagon a:h) {
			if(!rand.contains(a)) {
				set.add(a);
				
			}
		}
		
		for(Hexagon e: attackers.keySet()) {
			Hexagon richest = Collections.max(attackers.get(e));	

			if(e.getOwner() == 0) {
				moves = move((int) (boost*richest.getResources()), richest, e);
			}else if(e.getOwner() != getId()) {
				
			}
			
		}

		
		
		
		
		return moves;
	 */
	
	/*
	 * 	
		for(Hexagon r : rand) {
			for(Hexagon n: r.getNeighbours()) {
				if(n.getOwner() == 0) {
					free.put(n, 0);
				}else if(n.getOwner() != getId()) {
					if(n.getResources() > r.getResources())
						danger.put(n, 0);
					else
						free.put(n, 0);
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
	 */
	
	
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
