package hex.bots;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import hex.Hexagon;
import hex.Player;

public class CrazyBot extends Player{
	
	private double[] w;
	private double size;

	public CrazyBot(int id, int size, Color c, String name) {
		super(id, size, c, name);
		w = new double[] {0.7, 0.3 ,0.5, 0.7, 0.1};
		this.size = (2*size - 1)*(2*size - 1) - (size)*(size-1);
	}
	

	
	private double statevalue(HashSet<Hexagon> h) {
		double amount = (2*(double)h.size())/size;
		ArrayList<Hexagon> rand = rand(h);
		double randtot = 0, totres = 0;
		double enemies = 0,neutral = 0;
		double connection = 0;
		for(Hexagon r: rand) {
			randtot += r.getResources();
			enemies += enemies(r).size();
			neutral += neutral(r).size();
		}
		
		for(Hexagon a: h) {
			totres += a.getResources();
			connection += 1/(nonfriendly(a).size() + 0.00000001);
		}
		
		totres = totres/((double)h.size());
		
		randtot = randtot/(100.0*(double)rand.size());
		double en = enemies/(enemies + neutral);
		
		return w[0]*amount + w[1]*randtot + w[2]*en + w[3]*totres + w[4]*connection;
		
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
	
	private ArrayList<Hexagon> nonfriendly(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e : a.getNeighbours()) {
			if(e.getOwner() != getId())
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
	
	private HashSet<Hexagon> filledCopy(HashSet<Hexagon> h){
		HashSet<Hexagon> copy = new HashSet<Hexagon>();
		for(Hexagon a : h)
			copy.add(a);
		
		return copy;
	}
	
	private HashSet<Hexagon> fakeMove(Move m, HashSet<Hexagon> c){
		
		Hexagon boi = m.boi.clone();
		Hexagon target = m.target.clone();
		c.remove(boi);
		boi.setResources(boi.getResources() - m.res);
		c.add(boi);

		
		if(m.target.getOwner() != getId()) {
			target.setOwner(getId());
			if(target.getResources() <= m.res)
				target.setResources(m.res - target.getResources());
			else
				target.setResources(target.getResources() - m.res);
			c.add(target);
		}else {
			c.remove(target);
			target.setResources(m.res + target.getResources());
			c.add(target);
		}
		
		return c;
	}

	@Override
	public int[] algo(HashSet<Hexagon> H) {
		ArrayList<Hexagon> rand = rand(H);
		ArrayList<Hexagon> set = new ArrayList<Hexagon>();
		HashMap<Move, Double> moves = new HashMap<Move,Double>();;
		HashSet<Hexagon> copy;
		Move m;
		for(Hexagon h: H) {
			if(!rand.contains(h))
				set.add(h);
		}
		
		for(Hexagon r : rand) {
			for(Hexagon e: nonfriendly(r)) {
				m = new Move(r.getResources() - 1, r, e);
				copy = filledCopy(H);
				copy = fakeMove(m, copy);
				moves.put(m, statevalue(copy)); 
			}
		}
		
		for(Hexagon a : set) {
			for(Hexagon r: rand) {
				m = new Move(a.getResources() - 1, a, r);
				copy = filledCopy(H);
				copy = fakeMove(m, copy);
				moves.put(m, statevalue(copy)); 
			}
		}
		
		
		
		Map.Entry<Move, Double> bestmove = null;
		for(Map.Entry<Move, Double> e: moves.entrySet()) {
			if(bestmove == null || e.getValue().compareTo(bestmove.getValue()) > 0) {
				bestmove = e;
			}
		}

		return bestmove != null ? bestmove.getKey().getMove() : null;
	}
	
	private class Move{
		int[] movearr;
		Hexagon boi, target;
		int res;
		
		Move(int res, Hexagon n, Hexagon e) {
			this.res = res;
			this.boi = n;
			this.target = e;
			this.movearr = new int[] {getId(), res, n.getX(), n.getY(), e.getX(), e.getY()};
		}
		
		@Override
		public boolean equals(Object o) {
			boolean same = true;
			Move c = (Move) o;
			for(int i = 0; i < movearr.length; i++) {
				if(c.getMove()[i] != this.movearr[i]) {
					same = false;
					break;
				}
			}
			return same;
		}
		
		@Override
		public String toString() {
			return "Player: " + movearr[0] + " ( " + movearr[2] + " , " + movearr[3] + " ) ----> " + " ( " + movearr[4] + " , "
					+ movearr[5] + " ) " + " Res: " + movearr[1];
		}
		
		int[] getMove() {
			return this.movearr;
		}
		
		
		
	}

}
