package hex.bots;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import hex.Hexagon;
import hex.Player;

public class RandomBot extends Player{

	Random r;

	public RandomBot(int id, int size, Color c, String name) {
		super(id, size, c, name);
		r = new Random();
	}

	//id, res, x, y, targetX, targetY

	@Override
	public int[] algo(HashSet<Hexagon> H) {
		if(H.isEmpty())
			return null;
		
		Iterator<Hexagon> itr = H.iterator();
		Hexagon boi = itr.next();
		for(int i = 0; i < r.nextInt(H.size()); i++) {
			boi = itr.next();
		}
		
		Hexagon n = boi.getNeighbours()[r.nextInt(6)];
			
		return new int[] {getId(), r.nextInt(boi.getResources()), boi.getX(), boi.getY(), n.getX(), n.getY()};
	}

}
