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

	public CrazyBot(int id, int size, Color c, String name , double[] weights) {
		super(id, size, c, name);
		this.w = weights;
		this.size = (2*size - 1)*(2*size - 1) - (size)*(size-1);
		
	}
	
	
	
	private double statevalue(HashSet<Hexagon> h) {
		double indanger = 0;
		double setsize = (double)h.size();
		double amount = setsize/size;
		ArrayList<Hexagon> rand = rand(h);
		double randtot = 0;
		double connection = 0;
		double enres = 0;
		for(Hexagon r: rand) {
			randtot += r.getResources();
			for(Hexagon e: enemies(r)) {
				enres += e.getResources();
				if(e.getResources() > r.getResources())
					indanger++;
			}

		}
		
		for(Hexagon a: h) {
			connection += (6 - nonfriendly(a).size())/6.0;
		}
		
		indanger = -indanger/setsize;
		
		connection = connection/setsize;
				
		randtot = randtot/(100.0*(double)rand.size());
		
		enres = -enres/randtot;
		
		//System.out.println("amount: " + amount);
		/*System.out.println("randtot: " + randtot);
		System.out.println("easy: " + easy);
		System.out.println("totres: " + totres);
		System.out.println("conn: " + connection);
		System.out.println("--------------");
		*/
		
		return w[0]*amount + w[1]*connection + w[2]*indanger;
		
	}
	
	private ArrayList<Hexagon> rand(HashSet<Hexagon> h){
		ArrayList<Hexagon> rand = new ArrayList<Hexagon>();
		for(Hexagon a : h) {
			boolean r = false;
			for(Hexagon n : a.getNeighbours()) {
				if(n == null || n.getOwner() != getId()) {
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
			if(e != null && e.getOwner() == 0)
				enemies.add(e);
		}
		return enemies;
	}
	
	private ArrayList<Hexagon> nonfriendly(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e : a.getNeighbours()) {
			if(e != null && e.getOwner() != getId())
				enemies.add(e);
		}
		return enemies;
	}
	
	private ArrayList<Hexagon> enemies(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e : a.getNeighbours()) {
			if(e != null && e.getOwner() != getId() && e.getOwner() != 0)
				enemies.add(e);
		}
		return enemies;
	}
	
	private HashSet<Hexagon> filledCopy(HashSet<Hexagon> h){
		HashSet<Hexagon> copy	 = new HashSet<Hexagon>();
		for(Hexagon a : h)
			copy.add(a.clone());
		
		return copy;
	}
	
	private HashSet<Hexagon> fakeMove(Move m, HashSet<Hexagon> c){
		
		Hexagon boi = m.boi.clone();
		Hexagon target = m.target.clone();
		c.remove(boi);

		boi.setResources(boi.getResources() - m.res);
		c.add(boi);
		
		if(target.getOwner() != getId()) {
			if(target.getResources() <= m.res) {
				target.setResources(m.res - target.getResources() );
				target.setOwner(getId());
			}else
				target.setResources(target.getResources() - m.res);
			
			c.add(target);
		}else {
			c.remove(target);
			target.setResources(m.res + target.getResources() );
			c.add(target);
		}
		
		for(Hexagon a: c)
			a.setResources(a.getResources() + 1);
				
		return c;
	}

	@Override
	public int[] algo(HashSet<Hexagon> H) {
		
		if(H.isEmpty())
			return null;
		
		ArrayList<Hexagon> rand = rand(H);
		HashMap<Move, Double> moves = new HashMap<Move,Double>();
		HashSet<Hexagon> copy;
		Move m;

		
		for(Hexagon r : rand) {
			for(Hexagon e: nonfriendly(r)) {
				m = new Move(r.getResources() - 1, r, e);
				copy = filledCopy(H);
				copy = fakeMove(m, copy);
				moves.put(m, statevalue(copy)); 
			}
		}
		
		for(Hexagon a : H) {
			for(Hexagon r: rand) {
				if(!r.equals(a)) {
					m = new Move(a.getResources() - 1, a, r);
					copy = filledCopy(H);
					copy = fakeMove(m, copy);
					moves.put(m, statevalue(copy)); 
				}
			}
		}
		
		
		//System.out.println("------------------------");
		
		Map.Entry<Move, Double> bestmove = null;
		for(Map.Entry<Move, Double> e: moves.entrySet()) {
			//System.out.println("Crazy Idea : " + e.getKey() + " v: " + e.getValue());
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
