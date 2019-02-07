package hex;

public class Player {
	private int id;
	
	private Hexagon[][] myMap;
	
	public Player(int id,int size) {
		this.id = id;
		myMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
	}
	
	public void getMap(Hexagon[][] h){
		
		
		
	}
	
	
	public int getId() {
		return id;
	}
}
