package hex;

public class SimpleBot extends Player{

	public SimpleBot(int id, int size) {
		super(id, size);
	}
	
	public void algo(HexaMap H) {
		this.getMap(H);
	}

}
