package hex;

public class Hexagon {
	private int owner;
	private int resources;
	
	public Hexagon() {
		this.owner = 0;
		this.resources = 0;
	}
	
	public Hexagon(int owner) {
		this.owner = owner;
		this.resources = 0;
	}
	
	public int getResources() {
		return resources;
	}
	
	public int getOwner() {
		return owner;
	}
	
	
}
