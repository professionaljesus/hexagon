package hex.bots;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.evo.NEAT.Genome;

import hex.HexaMap;
import hex.Hexagon;
import hex.Player;

public class CrazyBot extends Player{
	
	private double[] w;
	private double size;
	private Genome gene;

	public CrazyBot(int id, int size, Color c, String name , Genome gene) {
		super(id, size, c, name);
		this.gene = gene;
		this.size = (2*size - 1)*(2*size - 1) - (size)*(size-1);
		
	}
	
	
	
	private float statevalue(HashSet<Hexagon> h) {
		double indanger = 0;
		double setsize = (double)h.size();
		double amount = setsize/size;
		ArrayList<Hexagon> rand = rand(h);
		double connection = 0;
		for(Hexagon r: rand) {
			for(Hexagon e: enemies(r)) {
				if(e.getResources() > r.getResources())
					indanger++;
			}
		}
		
		for(Hexagon a: h) {
			connection += (6 - nonfriendly(a).size())/6.0;
		}
		
		indanger = -indanger/setsize;
		
		connection = connection/setsize;
		
		float[] inputs = new float[] {(float) amount,(float)connection,(float)indanger};
		
		
						
		return gene.evaluateNetwork(inputs)[0];
		
	}
	
	private ArrayList<Hexagon> rand(HashSet<Hexagon> h){
		ArrayList<Hexagon> rand = new ArrayList<Hexagon>();
		for(Hexagon a : h) {
			boolean r = false;
			for(Hexagon n : a.getNeighbours(getId())) {
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
		for(Hexagon e : a.getNeighbours(getId())) {
			if(e != null && e.getOwner() == 0)
				enemies.add(e);
		}
		return enemies;
	}
	
	private ArrayList<Hexagon> nonfriendly(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e : a.getNeighbours(getId())) {
			if(e == null || e.getOwner() != getId())
				enemies.add(e);
		}
		return enemies;
	}
	
	private ArrayList<Hexagon> enemies(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e : a.getNeighbours(getId())) {
			if(e != null && e.getOwner() != getId() && e.getOwner() != 0)
				enemies.add(e);
		}
		return enemies;
	}
		
	private HashSet<Hexagon> fakeMove(Move m, HashSet<Hexagon> c){
		
		Hexagon boi = m.boi.clone();
		boi.setNeighbours(m.boi.getNeighbours(getId()));
		Hexagon target = m.target.clone();
		target.setNeighbours(m.target.getNeighbours(getId()));

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
		HashMap<Move, Float> moves = new HashMap<Move,Float>();
		HashSet<Hexagon> copy;
		Move m;

		
		for(Hexagon r : rand) {
			for(Hexagon e: nonfriendly(r)) {
				m = new Move(r.getResources() - 1, r, e);
				copy = HexaMap.getClonedPhex().get(this.getId() - 1);
				copy = fakeMove(m, copy);
				moves.put(m, statevalue(copy)); 
			}
		}
		
		for(Hexagon a : H) {
			for(Hexagon r: rand) {
				if(!r.equals(a)) {
					m = new Move(a.getResources() - 1, a, r);
					copy = HexaMap.getClonedPhex().get(this.getId() - 1);
					copy = fakeMove(m, copy);
					moves.put(m, statevalue(copy)); 
				}
			}
		}
		
		
		//System.out.println("------------------------");
		
		Map.Entry<Move, Float> bestmove = null;
		for(Map.Entry<Move, Float> e: moves.entrySet()) {
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
