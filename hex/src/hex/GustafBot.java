package hex;

import java.util.ArrayList;

public class GustafBot extends Player{
	
	ArrayList<Hexagon> rand;
	ArrayList<Hexagon> set;

	public GustafBot(int id, int size) {
		super(id, size);
		rand = new ArrayList<Hexagon>();
		set = new ArrayList<Hexagon>();

	}

	@Override
	public int[] algo(HexaMap H) {
		int[] moves;
		
		this.getMap(H);
		
		Hexagon[][] m = this.myMap;
		
		
		for(int x = 0; x < m.length; x++) {
			for(int y = 0; y < m[x].length; y++) {
				if(m[x][y] != null) {
					if(m[x][y].getOwner() == this.id) {
						set.add(m[x][y]);
						boolean r = false;
						Hexagon n;
						n = H.GetNeighbour(x, y, 0);
						for(int i = 1; i < 6; i++) {
							if(n.getOwner() != this.id) {
								r = true;
								i = 6;
							}else {
								n = H.GetNeighbour(x, y, i);
							}
						}
						
						if(r)
							rand.add(m[x][y]);
					}
						
				}
			}
		}
		
		
		
		for(Hexagon r : rand) {
			if(r.getResources() < 70) {
				for(Hexagon s: set) {
					if(!rand.contains(s))
						
						
				}
			}
		}
			

		
		return moves;
		
	}

	
}
