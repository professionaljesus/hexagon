package hex.bots;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import hex.Hexagon;
import hex.Player;

public class Snake extends Player{
	
	
	private Hexagon head;
	private ArrayList<Hexagon> done;
	
	public Snake(int id, int size, Color c, String name) {
		super(id, size, c, name);
		done = new ArrayList<Hexagon>();
	}
	
	private int[] move(int res, Hexagon n, Hexagon e) {
		return new int[] {getId(), res, n.getX(), n.getY(), e.getX(), e.getY()};
	}
	
	private ArrayList<Hexagon> enemynei(Hexagon a){
		ArrayList<Hexagon> enemies = new ArrayList<Hexagon>();
		for(Hexagon e: a.getNeighbours()) {
			if(e.getOwner() != getId())
				enemies.add(e);
		}
		return enemies;
	}

	@Override
	public int[] algo(HashSet<Hexagon> H) {
		
		Hexagon head = Collections.max(H);
		
		for(Hexagon h : H) {
			if(!h.equals(head) && !done.contains(h)) {
				done.add(h);
				return move(h.getResources() - 1, h, head);	
			}
		}
		done.clear();
		Hexagon target = Collections.min(enemynei(head));
		return move(head.getResources() - 1, head, target);
		
			
	}

}	
