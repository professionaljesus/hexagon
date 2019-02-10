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
					if(m[x][y].getOwner() == this.id) {
						boolean r = false;
						Hexagon n;
						n = H.GetNeighbour(x, y, 0);
						for(int i = 1; i < 6; i++) {
							if(n.getOwner() == 0) {
								r = true;
								i = 6;
							}else {
								n = H.GetNeighbour(x, y, i);
							}
						}
						
						if(r)
							rand.add(new int[] {x,y});
					}
						
				}
			}
		}
			

		
		return moves;
		
	}

	
}
