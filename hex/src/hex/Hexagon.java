package hex;


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
