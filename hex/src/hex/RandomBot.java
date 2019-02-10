package hex;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class RandomBot extends Player{

	Random r;

	public RandomBot(int id, int size, Color c) {
		super(id, size, c);
		r = new Random();
	}

	//id, res, x, y, targetX, targetY

	@Override
	public int[] algo(HashSet<Hexagon> H) {

		Iterator<Hexagon> itr = H.iterator();
		Hexagon boi = itr.next();
		for(int i = 0; i < r.nextInt(H.size()); i++) {
			boi = itr.next();
		}
		
		Hexagon n = boi.getNeighbours()[r.nextInt(6)];
		
			
			
		return new int[] {getId(), r.nextInt(boi.getResources()), boi.getX(), boi.getY(), n.getX(), n.getY()};
	}

}
