package hex;

import java.awt.Color;
import java.util.ArrayList;

public class GustafBot extends Player{
	
	ArrayList<Hexagon> rand;
	ArrayList<Hexagon> set;

	public GustafBot(int id, int size, Color c) {
		super(id, size, c);
		rand = new ArrayList<Hexagon>();
		set = new ArrayList<Hexagon>();

	}

	@Override
	public int[] algo(HexaMap H) {
		int[] moves = null;
		
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
			if(moves == null) {
				for(Hexagon n: r.getNeighbours(H)) {
					if(n.getOwner() == 0) {
						moves = new int[] {this.id, 6, (3*r.getResources() )/ 4, r.getX(), r.getY(), n.getX(), n.getY()};
					}
				}
				
				
				
				if(r.getResources() < 70) {
					for(Hexagon s: set) {
						if(!rand.contains(s)) {
							moves = new int[] {this.id, 6, (3*s.getResources() )/ 4, s.getX(), s.getY(), r.getX(), r.getY()};
						}
						
					}
				}
			}
		}
			

		
		return moves;
		
	}

	
}
