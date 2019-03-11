package com;

import java.util.ArrayList;
import java.util.HashSet;

import hex.Hexagon;

public class State{
	float amount, indanger, connection;
	int id;
	public State(float[] s, int id){
		this.id = id;
		this.amount = s[0];
		this.indanger = s[1];
		this.connection = s[2];
	}
	
	public int[] coordinates(){
		return new int[] {Math.round(amount*100),Math.round(indanger*100),Math.round(100*connection)};
	}
	
	public State(HashSet<Hexagon> h, int size){
		this.id = h.iterator().next().getOwner();
		indanger = 0;
		float setsize = (float)h.size();
		amount = setsize/size;
		ArrayList<Hexagon> rand = rand(h);
		connection = 0;
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
	}
	
	private ArrayList<Hexagon> rand(HashSet<Hexagon> h){
		ArrayList<Hexagon> rand = new ArrayList<Hexagon>();
		for(Hexagon a : h) {
			boolean r = false;
			for(Hexagon n : a.getNeighbours(id)) {
				if(n == null || n.getOwner() != (id)) {
					r = true;
					break;
				}
			}
			if(r)
				rand.add(a);
		}
		return rand;
	}
	
	@Override
	public String toString() {
		return amount + "," + indanger + "," + connection;
	}
	
	@Override
	public boolean equals(Object o) {
		State s = (State) o;
		return this.amount == s.amount && this.connection == s.connection && this.indanger == s.connection;
	}
	
	public double dist(State s) {
		return Math.sqrt(Math.pow(s.amount - this.amount,2) + Math.pow(s.indanger - this.indanger,2) + Math.pow(s.connection - this.connection,2));
	}

	public double dist(HashSet<Hexagon> h, int size) {
		float oindanger = 0;
		float setsize = (float)h.size();
		float oamount = setsize/size;
		ArrayList<Hexagon> rand = rand(h);
		float oconnection = 0;
		for(Hexagon r: rand) {
			for(Hexagon e: enemies(r)) {
				if(e.getResources() > r.getResources())
					oindanger++;
			}
		}
		
		for(Hexagon a: h) {
			oconnection += (6 - nonfriendly(a).size())/6.0;
		}
		
		oindanger = -indanger/setsize;
		oconnection = connection/setsize;
		
		
		return Math.sqrt(Math.pow(oamount - this.amount,2) + Math.pow(oindanger - this.indanger,2) + Math.pow(oconnection - this.connection,2));
	}
	
	private ArrayList<Hexagon> neutral(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e : a.getNeighbours(id)) {
			if(e != null && e.getOwner() == 0)
				enemies.add(e);
		}
		return enemies;
	}
	
	private ArrayList<Hexagon> nonfriendly(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e : a.getNeighbours(id)) {
			if(e == null || e.getOwner() != id)
				enemies.add(e);
		}
		return enemies;
	}
	
	private ArrayList<Hexagon> enemies(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e : a.getNeighbours(id)) {
			if(e != null && e.getOwner() != id && e.getOwner() != 0)
				enemies.add(e);
		}
		return enemies;
	}
}
