package hex.bots;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import hex.Hexagon;
import hex.Player;

public class CrazyBot extends Player{
	
	private double[] w;
	private double size;

	public CrazyBot(int id, int size, Color c, String name) {
		super(id, size, c, name);
		w = new double[] {0.5,0.5,0.5};
		this.size = (2*size - 1)*(2*size - 1) - (size)*(size-1);
	}
	
	private int[] move(int res, Hexagon n, Hexagon e) {
		return new int[] {getId(), res, n.getX(), n.getY(), e.getX(), e.getY()};
	}
	
	private double statevalue(HashSet<Hexagon> h) {
		double amount = (2*(double)h.size())/size;
		ArrayList<Hexagon> rand = rand(h);
		double randtot = 0;
		double enemies = 0,neutral = 0;
		for(Hexagon r: rand) {
			randtot += r.getResources();
			enemies += enemies(r).size();
			neutral += neutral(r).size();

		}
		
		randtot = randtot/(100.0*(double)rand.size());
		double en = enemies/(enemies + neutral);
		
		return w[0]*amount + w[1]*randtot + w[2]*en;
		
	}
	
	private ArrayList<Hexagon> rand(HashSet<Hexagon> h){
		ArrayList<Hexagon> rand = new ArrayList<Hexagon>();
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
		return rand;
	}
	
	private ArrayList<Hexagon> neutral(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e : a.getNeighbours()) {
			if(e.getOwner() == 0)
				enemies.add(e);
		}
		return enemies;
	}
	
	private ArrayList<Hexagon> enemies(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e : a.getNeighbours()) {
			if(e.getOwner() != getId() && e.getOwner() != 0)
				enemies.add(e);
		}
		return enemies;
	}

	@Override
	public int[] algo(HashSet<Hexagon> H) {
		ArrayList<Hexagon> rand = rand(H);
		ArrayList<Hexagon> set = new ArrayList<Hexagon>();
		for(Hexagon h: H) {
			if(!rand.contains(h))
				set.add(h);
		}
		ArrayList<int[]> moves = new ArrayList<int[]>();
		for(Hexagon r : rand) {
			HashSet<Hexagon> copy = new HashSet<Hexagon>();
			
			
			
		}

		return null;
	}

}
