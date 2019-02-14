package hex;

import java.util.HashSet;

public class Hexagon implements Comparable<Hexagon>{
	private int owner, x ,y;
	private int resources;
	private Hexagon[] neighbours;
	
	
	public Hexagon(int x, int y) {
		this.x = x;
		this.y = y;
		this.owner = 0;
		this.resources = 0;
	}
	
	public Hexagon[] getNeighbours() {
		return neighbours;
	}
	
	public void setNeighbours(Hexagon[] n) {
		neighbours = new Hexagon[6];
		for(int i = 0; i < n.length; i++) {
			neighbours[i] = n[i];
		}
	}
	
	@Override
	public String toString() {
		return "(" + getX() + "," + getY() + ")";
		
	}
	
	public int getResources() {
		return resources;
	}
	
	public void setResources(int resources) {
		this.resources = resources;
	}

	public void setOwner(int owner) {
		
		this.owner = owner;
	}
	
	public int getOwner() {
		return owner;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int calculateDistanceTo(Hexagon target){
		if(target.equals(this)){
			return 0;
		}
		int dist = 0;
		boolean foundTarget = false;
		HashSet<Hexagon> scanned = new HashSet<Hexagon>();
		HashSet<Hexagon> newlyScanned = new HashSet<Hexagon>();
		HashSet<Hexagon> outers = new HashSet<Hexagon>();
		scanned.add(this);
		outers.add(this);
		while(!foundTarget){
			newlyScanned.clear();
			dist++;
			for(Hexagon outer:outers){
				Hexagon[] neighbours = outer.getNeighbours();
				for(int i = 0; i<6; i++){
					if(target.equals(neighbours[i])){
						foundTarget = true;
					}
					if(!scanned.contains(neighbours[i])){
						newlyScanned.add(neighbours[i]);
					}
				}
			}
			scanned.addAll(newlyScanned);
			outers.clear();
			outers.addAll(newlyScanned);

		}
		return dist;
	}
	
	@Override
	public Hexagon clone() {
		Hexagon n = new Hexagon(this.x,this.y);
		n.setOwner(this.owner);
		n.setResources(this.resources);
		n.setNeighbours(this.neighbours);
		return n;
		
	}
	
	@Override
	public boolean equals(Object o) {
		Hexagon c = (Hexagon) o;
		if(c.getX() == x && c.getY() == y)
			return true;
		else
			return false;
	}



	@Override
	public int compareTo(Hexagon o) {
		return this.resources - o.getResources();
	}


}
