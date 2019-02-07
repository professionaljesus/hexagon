package hex;

public class Hexagon {
	private int owner;
	private int resources;
	
	public Hexagon() {
		this.owner = 0;
		this.resources = 0;
	}
	
	public Hexagon(int owner,int resources) {
		this.owner = owner;
		this.resources = resources;
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
	
	
	
}
