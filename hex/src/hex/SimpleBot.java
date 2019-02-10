package hex;

public class SimpleBot extends Player{

	public SimpleBot(int id, int size) {
		super(id, size);
	}
	
	public int[] algo(HexaMap H) {
		super.getMap(H);
		int[] moves = new int[5];
		moves[0] = super.getId();
		moves[1] = 0;
		moves[3] = 0;
		moves[4] = 3;
		moves[2] = super.myMap[0][3].getResources()/10;
		//this.getMap(H);
		return moves;
		//g
		
	}
	
	

}
