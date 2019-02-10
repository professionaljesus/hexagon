package hex;

import java.util.ArrayList;

public class GustafBot extends Player{
	
	ArrayList<int[]> rand;

	public GustafBot(int id, int size) {
		super(id, size);
		rand = new ArrayList<int[]>();
	}

	@Override
	public int[] algo(HexaMap H) {
		int[] moves = new int[5];

		this.getMap(H);
		
		
		return moves;
		
	}

	
}
