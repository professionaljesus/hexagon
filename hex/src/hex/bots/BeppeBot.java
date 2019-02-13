package hex.bots;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import hex.Hexagon;
import hex.Player;

public class BeppeBot extends Player {

	public BeppeBot(int id, int size, Color c, String name) {
		super(id, size, c, name);
	}

	// id, res, x, y, targetX, targetY
	@Override
	public int[] algo(HashSet<Hexagon> H) {
		ArrayList<int[]> moves = new ArrayList<int[]>();

		HashSet<Hexagon> neighbours = new HashSet<Hexagon>();
		HashSet<Hexagon> outers = new HashSet<Hexagon>();
		HashSet<Hexagon> inners = new HashSet<Hexagon>();

		// Skapar hashet med mina grannar och mina yttre hexagoner
		for (Hexagon hex : H) {
			Hexagon[] neighs = hex.getNeighbours();
			for (int i = 0; i < 6; i++) {
				if (neighs[i].getOwner() != getId()) {
					neighbours.add(neighs[i]);
					outers.add(hex);
				} else {
					inners.add(hex);
				}
			}
		}

		// Beräkna alla tillåtna erövringar
		for (Hexagon hex : H) {
			Hexagon[] neighs = hex.getNeighbours();
			for (int i = 0; i < 6; i++) {
				if (neighbours.contains(neighs[i])) {
					// Om hexagonen går att erövra
					if (hex.getResources() > neighs[i].getResources()) {
						// res,x,y,targetX,targetY, value
						int[] move = new int[6];
						move[0] = hex.getResources() - 1;
						move[1] = hex.getX();
						move[2] = hex.getY();
						move[3] = neighs[i].getX();
						move[4] = neighs[i].getY();
						move[5] = hex.getResources() - neighs[i].getResources();
						moves.add(move);
					}

				}
			}
		}

		// res,x,y,targetX,targetY, value
		int[] flytt = new int[6];
		Hexagon sourceHex;
		// Truppförflyttningar
		for (Hexagon out : outers) {
			Hexagon[] neighs = out.getNeighbours();
			int largestEnemyHex = 0;
			for (int i = 0; i < 6; i++) {
				if (neighs[i].getOwner() != getId() && neighs[i].getResources() > largestEnemyHex) {
					largestEnemyHex = neighs[i].getResources();
					flytt[3] = neighs[i].getX();
					flytt[4] = neighs[i].getY();
				}
			}
		}

		if (!inners.isEmpty()) {
			flytt[0] = Collections.max(inners).getResources() - 1;
			flytt[1] = Collections.max(inners).getX();
			flytt[2] = Collections.max(inners).getY();
			flytt[5] = 0;
			moves.add(flytt);
		}

		int[] tempMove = null;
		int maxValue = -1;
		for (int i = 0; i < moves.size(); i++) {
			if (moves.get(i)[5] > maxValue) {
				maxValue = moves.get(i)[5];
				tempMove = moves.get(i);
			}
		}

		// id, res, x, y, targetX, targetY
		if(tempMove == null){
			return null;
		}else{
			return new int[] { getId(), tempMove[0], tempMove[1], tempMove[2], tempMove[3], tempMove[4] };
		}
		
	}

}
