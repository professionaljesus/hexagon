package hex;

import java.awt.Color;

public class Hexagon {
	private int owner;
	private int resources;
	private Color color;
	
	
	public Hexagon() {
		this.owner = 0;
		this.resources = 0;
		color = Color.BLACK;
	}
	
	public Hexagon(int owner,int resources,Color color) {
		this.owner = owner;
		this.resources = resources;
		this.color = color;
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
	
	
}
