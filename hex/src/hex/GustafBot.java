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
		
		Hexagon[][] m = this.myMap;
		
		
		for(int x = 0; x < m.length; x++) {
			for(int y = 0; y < m[x].length; y++) {
				if(m[x][y] != null) {
					if(m[x][y].getOwner() == this.id)
						H.GetNeighbour(x, y, 1);
				}
			}
		}
			

		
		return moves;
		
	}

	
}
