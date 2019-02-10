package hex;

import java.awt.Color;

public class Hexagon {
	private int owner, x ,y;
	private int resources;
	private Color color;
	
	
	public Hexagon(int x, int y) {
		this.x = x;
		this.y = y;
		this.owner = 0;
		this.resources = 0;
		color = Color.BLACK;
	}
	
	public Hexagon[] getNeighbours(HexaMap h) {
		return h.GetNeighbours(x, y);
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
	
	public String toString(){
		return Integer.toString(resources);
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
	
	
}
