package hex.bots;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.Boi;
import com.Q;
import com.State;

import hex.HexaMap;
import hex.Hexagon;
import hex.Player;

public class QBert extends Player{
	
	private int size;
	private ArrayList<Integer> aot;
	private float[][][][] Q;
	private Random random;
	private int acts, round;
	private double discount;
	private ArrayList<int[]> states;
	private ArrayList<Integer> actions;

	public QBert(int id, int size, Color c, String name, Q q) {
		super(id, size, c, name);
		this.size = (2*size - 1)*(2*size - 1) - (size)*(size-1);
		this.aot = new ArrayList<Integer>();
		this.actions = new ArrayList<Integer>();
		this.states = new ArrayList<int[]>();
		this.Q = q.getMap();
		this.acts = Q[0][0][0].length;
		this.random = new Random();
		this.discount = 0.3;
		this.round = 0;
	}
	
	private int[] coordinates(HashSet<Hexagon> h) {
		float setsize = (float)h.size();
		float amount = setsize/size;
		float indanger = 0;
		ArrayList<Hexagon> indangerhexagons = new ArrayList<Hexagon>();
		ArrayList<Hexagon> rand = rand(h);
		float connection = 0;
		for(Hexagon r: rand) {
			for(Hexagon e: enemies(r)) {
				if(!indangerhexagons.contains(r) && e.getResources() > r.getResources())
					indangerhexagons.add(r);
			}
		}
		
		for(Hexagon a: h) {
			connection += (6 - nonfriendly(a).size())/6.0;
		}
		
		indanger = ((float) indangerhexagons.size())/setsize;
		
		connection = connection/setsize;
		
		int x = Math.round(amount*100);
		int y = Math.round(indanger*100);
		int z = Math.round(100*connection);
		
		if(x > Q.length) {
			System.out.println("x " + x);
			x = Q.length - 1;
		}
		if(y > Q[0].length) {
			System.out.println("y " + y);
			y = Q[0].length - 1;
		}
		if(z > Q[0][0].length) {
			System.out.println("z " + z);
			z = Q[0][0].length - 1;
		}
		
		return new int[] {x,y,z};
		
	}
	
	private void updateQ(int[] current) {
		int b = 3;
		if(round >= b)
			Q[states.get(round - b)[0]][states.get(round - b)[1]][states.get(round - b)[2]][actions.get(round - b)] += aot.get(round - 1) - aot.get(round - b) + discount*maxReward(states.get(round - 1))[1];
	}
	
	/*
	 * 0 - boost most in danger
	 * 1 - attack least protected not owned hex
	 * 2 - attack easiest enemy
	 * 3 - attack enemy with least friends
	 */
	
	private Move action(int a, HashSet<Hexagon> h) {
		actions.set(actions.size() - 1, a);
		ArrayList<Hexagon> rand = rand(h);
		ArrayList<Hexagon> set = new ArrayList<Hexagon>();
		for(Hexagon boi : h) {
			if(!rand.contains(boi))
				set.add(boi);
		}
		
		Collections.sort(set, Collections.reverseOrder());
		
		int max = 0, min = 10000;
		Hexagon attacker = null;
		Hexagon target = null;
		int resources = 0;


		switch(a) {
		case 0:
			for(Hexagon r: rand) {
				int indanger = 0;
				for(Hexagon e: enemies(r)) {
					if(e.getResources() > r.getResources())
						indanger++;
				}
				if(target == null || indanger > max) {
					max = indanger;
					target = r;
				}
			}
			if(!set.isEmpty())
				resources = set.get(0).getResources() - 1;
			break;
		case 1:
			
			for(Hexagon r : rand) {
				for(Hexagon n : neutral(r)) {
					if(target == null  || enemies(n).size() < min) {
						target = n;
						attacker = r;
						min = enemies(n).size();
					}
				}
			}
			if(attacker != null)
				resources = attacker.getResources() - 1;
			break;
		case 2:
			for(Hexagon r : rand) {
				for(Hexagon e: enemies(r)) {
					if(target == null || (e.getResources() < min && e.getResources() < r.getResources())) {
						min = e.getResources();
						target = e;
						attacker = r;
					}
					if(attacker != null && target.equals(e) && attacker.getResources() < r.getResources())
						attacker = r;
						
				}
					
			}
			if(attacker != null)
				resources = attacker.getResources() - 1;
			break;
		case 3:
			for(Hexagon r : rand) {
				for(Hexagon e: enemies(r)) {
					if(target == null || (enemies(e).size() < min && e.getResources() < r.getResources())) {
						min = enemies(e).size();
						target = e;
						attacker = r;
					}
					if(attacker != null && target.equals(e) && attacker.getResources() < r.getResources())
						attacker = r;
						
				}
					
			}
			if(attacker != null)
				resources = attacker.getResources() - 1;
			break;
			
		default:
			return null;
			
		}
		if(resources != 0 && attacker != null && target != null)
			return new Move(resources, attacker, target);
		else
			return action(random.nextInt(acts),h);


	}
	
	
	private int whatDo(int[] state) {
		if(random.nextDouble() < 0.25)
			return random.nextInt(acts);
		
		return (int)maxReward(state)[0];
			
	}
	
	private float[] maxReward(int[] state) {
		
		float[] actionlist = Q[state[0]][state[1]][state[2]];
		float max = 0;
		int action = random.nextInt(actionlist.length);
		for(int i = 0; i < actionlist.length; i++) {
			if(actionlist[i] > max) {
				max = actionlist[i];
				action = i;
			}
		}
		return new float[] {(float)action,max};
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
		

	@Override
	public int[] algo(HashSet<Hexagon> H) {
		if(H.isEmpty())
			return null;
		
		aot.add(H.size());
		states.add(coordinates(H));
		actions.add(1);
		round++;

		updateQ(states.get(round - 1));


		return action(whatDo(states.get(round - 1)), H).movearr;
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
