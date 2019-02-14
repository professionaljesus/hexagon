package hex.bots;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import hex.Hexagon;
import hex.Player;

public class Bepster extends Player {

	HashSet<Hexagon> neighbours = new HashSet<Hexagon>();
	HashSet<Hexagon> outers = new HashSet<Hexagon>();
	HashSet<Hexagon> inners = new HashSet<Hexagon>();

	public Bepster(int id, int size, Color c, String name) {
		super(id, size, c, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] algo(HashSet<Hexagon> H) {
		// Creates hashsets with my neighbouring, outer and inner hexagons
		for (Hexagon hex : H) {
			Hexagon[] neighs = hex.getNeighbours();
			boolean inner = true;
			for (int i = 0; i < 6; i++) {
				if (neighs[i].getOwner() != getId()) {
					neighbours.add(neighs[i]);
					outers.add(hex);
					inner = false;
				}
			}
			if (inner == true) {
				inners.add(hex);
			}
		}
		
		
		

		return null;
	}
	
	private int[] bestCapture(){
		int[] move = new int[5];
		for(Hexagon outer: outers){
			Hexagon[] nei = outer.getNeighbours();
			for(Hexagon n: nei){
				if(neighbours.contains(n)){
					
				}
			}
		}
		return move;
	}

}
