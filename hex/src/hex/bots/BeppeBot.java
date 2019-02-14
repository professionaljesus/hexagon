package hex.bots;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import hex.Hexagon;
import hex.Player;

public class BeppeBot extends Player {
	//
	private static boolean printMoveOptions = false;

	HashSet<Hexagon> neighbours = new HashSet<Hexagon>();
	HashSet<Hexagon> outers = new HashSet<Hexagon>();
	HashSet<Hexagon> inners = new HashSet<Hexagon>();
	HashSet<Hexagon> freebies = new HashSet<Hexagon>();
	HashSet<Hexagon> inNeedOfOffensiveSupport = new HashSet<Hexagon>();
	int counter = 0;

	public BeppeBot(int id, int size, Color c, String name) {
		super(id, size, c, name);
	}

	// id, res, x, y, targetX, targetY
	@Override
	public int[] algo(HashSet<Hexagon> H) {
		ArrayList<int[]> moves = new ArrayList<int[]>();
		// ArrayList<HashSet<Hexagon>> layers = new
		// ArrayList<HashSet<Hexagon>>();
		
		counter+=1;
		
		neighbours.clear();
		outers.clear();
		inners.clear();
		freebies.clear();
		inNeedOfOffensiveSupport.clear();

		// Creates hashsets with my neighbouring, outer and inner hexagons
		for (Hexagon hex : H) {
			Hexagon[] neighs = hex.getNeighbours();
			boolean inner = true;
			for (int j = counter%6; j < 6+counter%6; j++) {
				int i=j%6;
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

		// Sort out the freebies
		for (Hexagon hex : neighbours) {
			if (hex.getOwner() == 0) {
				freebies.add(hex);
			}
		}

		// Calculate all valid captures
		for (Hexagon source : H) {
			Hexagon[] neighs = source.getNeighbours();
			for (int i = 0; i < 6; i++) {
				if (neighbours.contains(neighs[i])) {
					Hexagon target = neighs[i];
					int[] move = new int[7];
					move[1] = source.getX();
					move[2] = source.getY();
					move[3] = target.getX();
					move[4] = target.getY();

					// Om hexagonen g�r att er�vra utan att sl�ppa defence p�
					// source hexagonen
					if (source.getResources() > target.getResources()) {
						// res,x,y,targetX,targetY, value
						move[0] = source.getResources()-1;
						move[5] = calculateValueOfCapture(source, target);
						if(source.getResources()>100){
							move[5] +=100;
						}
						move[6] = 0;
						moves.add(move);
					} else if (freebies.contains(target) && source.getResources() > 1) {
						move[0] = (int) (source.getResources() / 2);
						move[5] = calculateValueOfCapture(source, target);
						move[6] = 0;
						moves.add(move);
					}
				}
			}
		}
		
		for(Hexagon hex: outers){
			Hexagon[] nei = hex.getNeighbours();
			for(int i = 0; i<6; i++){
				System.out.println(hex.calculateDistanceTo(hex));
			}
		}

		// Troopmovements
		Hexagon source = null;
		if (!inners.isEmpty()) {
			source = Collections.max(inners);
		}
		for (Hexagon out : outers) {
			if (source == null) {
				source = Collections.max(outers);
			} else if (out.getResources() > 5 * source.getResources()) {
				source = Collections.max(outers);
			}
		}

		// Defensive support
		Hexagon target = null;
		int LargestThreat = 0;
		for (Hexagon out : outers) {
			if (calculateThreat(out) > LargestThreat && !out.equals(source)) {
				LargestThreat = calculateThreat(out);
				target = out;
			}
		}

		// res,x,y,targetX,targetY, value
		if (target != null && source != null && calculateSizeOfTroopTransferThatEnsuresDefence(source, target) > 0) {
			int[] flytt = new int[7];
			flytt[0] = calculateSizeOfTroopTransferThatEnsuresDefence(source, target);
			flytt[1] = source.getX();
			flytt[2] = source.getY();
			flytt[3] = target.getX();
			flytt[4] = target.getY();
			flytt[5] = (int) ((LargestThreat - target.getResources()) * 0.2);
			flytt[6] = 1;
			moves.add(flytt);
		}

		// Offensive support
		for (Hexagon hex : outers) {
			int max = 0;
			if (hex.getResources()-calculateLowestThreat(hex) > max && !hex.equals(source)) {
				target = hex;
				max = hex.getResources()-calculateLowestThreat(target);
			}
		}

		// res,x,y,targetX,targetY, value
		if (target != null && source != null && calculateSizeOfTroopTransferThatEnsuresDefence(source, target) > 0) {
			int[] flytt = new int[7];
			flytt[0] = calculateSizeOfTroopTransferThatEnsuresDefence(source, target);
			flytt[1] = source.getX();
			flytt[2] = source.getY();
			flytt[3] = target.getX();
			flytt[4] = target.getY();
			flytt[5] = calculateSizeOfTroopTransferThatEnsuresDefence(source, target)-calculateLowestThreat(target);
			flytt[6] = 2;
			moves.add(flytt);
		}

		// Choose the move with highest value
		int[] tempMove = null;
		int maxValue = -1;
		for (int i = 0; i < moves.size(); i++) {
			if (moves.get(i)[5] > maxValue) {
				maxValue = moves.get(i)[5];
				tempMove = moves.get(i);
			}
		}
		
		if(printMoveOptions){
			printMoves(moves);
		}
		

		// id, res, x, y, targetX, targetY
		if (tempMove == null) {
			return null;
		} else {
			return new int[] { getId(), tempMove[0], tempMove[1], tempMove[2], tempMove[3], tempMove[4] };
		}

	}
	
	

	private int calculateSizeOfTroopTransferThatEnsuresDefence(Hexagon source, Hexagon target) {
		return source.getResources() - calculateThreat(source) - 1;
	}

	private int calculateValueOfCapture(Hexagon source, Hexagon target) {
		
		int value = 0;
		value += source.getResources();
		value -= target.getResources();
		//value -= calculateThreat(target);
		value += calculateSupport(target);

		if (freebies.contains(target)) {
			value += 1000;
		}

		return (int) (value*Math.pow(0.9,target.getResources()));
	}

	private int calculateThreat(Hexagon target) {
		Hexagon[] nei = target.getNeighbours();
		int value = 0;
		for (int i = 0; i < nei.length; i++) {
			if (nei[i].getOwner() != getId() && neighbours.contains(nei[i]) && value < nei[i].getResources()) {
				value = nei[i].getResources();
			}
		}
		return value;
	}
	
	private int calculateLowestThreat(Hexagon target) {
		Hexagon[] nei = target.getNeighbours();
		int value = 1000;
		for (int i = 0; i < nei.length; i++) {
			if (nei[i].getOwner() != getId() && neighbours.contains(nei[i]) && value > nei[i].getResources()) {
				value = nei[i].getResources();
			}
		}
		return value;
	}

	private int calculateSupport(Hexagon target) {
		Hexagon[] nei = target.getNeighbours();
		int value = 0;
		for (int i = 0; i < nei.length; i++) {
			if (nei[i].getOwner() == getId()) {
				value += 1;
			}
		}
		return value;
	}

	private void printMoves(ArrayList<int[]> moves) {
		Collections.sort(moves, new Comparator<int[]>() {
			@Override
			public int compare(int[] arg0, int[] arg1) {
				// TODO Auto-generated method stub
				return arg1[5] - arg0[5];
			}
		});

		for (int i = 0; i < 5 && i < moves.size(); i++) {
			if (moves.get(i) != null) {
				String s = "(" + moves.get(i)[1] + "," + moves.get(i)[2] + ") --> (" + moves.get(i)[3] + ","
						+ moves.get(i)[4] + ") \tRes:" + moves.get(i)[0] + "\tValue:" + moves.get(i)[5];
				switch (moves.get(i)[6]) {
				case 0:
					s += "\t Capture";
					break;
				case 1:
					s += "\t Defensive Support";
					break;
				case 2:
					s += "\t Attacking Support";
					break;
				}
				System.out.println(s);
			}
		}
	}

	private void printHexes(HashSet<Hexagon> hexes) {
		for (Hexagon hex : hexes) {
			System.out.println("(" + hex.getX() + "," + hex.getY() + ")");
		}
	}

}
