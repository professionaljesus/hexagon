package hex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class SimpleBot extends Player {
	int turnorder;
	int own;
	int generating;
	int turn;
	private Hexagon[][] heatMap;
	private int[] heatdir;
	private Hexagon[][] myMap;
	private HashSet<Integer[]> Actionlist;
	private Random r;
	private HashMap<String, List<Integer[]>> map;
	private List<Integer[]> goodnei;
	private List<Integer[]> badnei;
	private List<Integer[]> hardcorenei;
	private List<Integer[]> easynei;
	private List<Integer[]> nonei;

	public SimpleBot(int id, int size, Color color) {
		super(id, size, color);
		r = new Random();
		myMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		heatMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		Actionlist = new HashSet<Integer[]>();
		map = new HashMap<>();
		goodnei = new ArrayList<Integer[]>();
		badnei = new ArrayList<Integer[]>();
		hardcorenei = new ArrayList<Integer[]>();
		easynei = new ArrayList<Integer[]>();
		nonei = new ArrayList<Integer[]>();
		for (int x = 0; x < heatMap.length; x++) {
			for (int y = 0; y < heatMap[x].length; y++) {
				if (x + y >= size - 1 && x + y <= size * 3 - 3) {
					heatMap[x][y] = new Hexagon(x, y);
					myMap[x][y] = new Hexagon(x, y);
				}
			}
		}
		switch (id) {
		case 1:
			heatMap[size - 1][size * 2 - 2].setOwner(2);
			heatMap[size - 1][size * 2 - 2].setResources(100);
			heatMap[size * 2 - 2][0].setOwner(3);
			heatMap[size * 2 - 2][0].setResources(100);

			myMap[0][size - 1].setOwner(1);
			myMap[0][size - 1].setResources(100);
			heatMap[0][size - 1].setOwner(1);
			heatMap[0][size - 1].setResources(100);
			break;
		case 2:
			heatMap[size * 2 - 2][0].setOwner(3);
			heatMap[size * 2 - 2][0].setResources(100);
			heatMap[0][size - 1].setOwner(1);
			heatMap[0][size - 1].setResources(100);

			myMap[size - 1][size * 2 - 2].setOwner(2);
			myMap[size - 1][size * 2 - 2].setResources(100);
			heatMap[size - 1][size * 2 - 2].setOwner(2);
			heatMap[size - 1][size * 2 - 2].setResources(100);
			break;
		case 3:
			heatMap[size - 1][size * 2 - 2].setOwner(1);
			heatMap[size - 1][size * 2 - 2].setResources(100);
			heatMap[0][size - 1].setOwner(2);
			heatMap[0][size - 1].setResources(100);

			myMap[size * 2 - 2][0].setOwner(3);
			myMap[size * 2 - 2][0].setResources(100);
			heatMap[size * 2 - 2][0].setOwner(3);
			heatMap[size * 2 - 2][0].setResources(100);
			break;
		}
		own = 1;
		generating = 0;
		turnorder = id;
		turn = 1;
	}

	public int[] algo(HashSet<Hexagon> H) {
		// Update data
		generating = 0;
		own = H.size();
		for (Hexagon h : H) {
			if(h == null){
				continue;
			}
			if (h.getResources() < 100) {
				generating++;
			}
			myMap[h.getX()][h.getY()] = h;
			for (Hexagon ne : h.getNeighbours()) {
				if(ne == null){
					break;
				}
				if (ne.getOwner() == super.getId()) {
					for(int i =1; i < 10; i++){
						
					if(h.getResources()/i ==0){
						break;
					}
					
					TurnMoves(h, ne, h.getResources()/i-1, 1);
					TurnMoves(h, ne, h.getResources()/i-1, 3);
					}
				} else if (ne.getOwner() == 0) {
					for(int i =1; i < 10; i++){
						
						if(h.getResources()/i ==0){
							break;
						}
						if(h.getResources()/i==1){
							TurnMoves(h, ne, h.getResources()/i, 0);
							
						}else
					TurnMoves(h, ne, h.getResources()/i-1, 0);
					}
				} else {
					for(int i =1; i < 10; i++){
						
						if(h.getResources()/i ==0){
							break;
						}
					TurnMoves(h, ne, h.getResources()/i-1, 2);
					}
				}
				// Random strat
				TurnMoves(h, ne, h.getResources(), 5);
				TurnMoves(h, ne, h.getResources(), 6);
				TurnMoves(h, ne, h.getResources(), 8);
				if(h.getOwner() != ne.getOwner()){
					TurnMoves(h, ne, h.getResources(), 7);
				}
			}

		}
		Integer[] Action = Collections.max(Actionlist, (e1, e2) -> e1[6].compareTo(e2[6]));
		Actionlist.clear();
		int[] realaction = new int[6];
		realaction[0] = super.getId();
		realaction[1] = Action[1];
		realaction[2] = Action[2];
		realaction[3] = Action[3];
		realaction[4] = Action[4];
		realaction[5] = Action[5];
		if(realaction[1] == 0){
			realaction[0] = super.getId();
			realaction[1] = 1;
			Hexagon a= H.iterator().next();
			if(a != null){
				realaction[2] = a.getX();
				realaction[3] = a.getY();
			}
			Hexagon b= H.iterator().next();
			if(b != null){
				realaction[4] = b.getX();
				realaction[5] = b.getY();
			}
		}
		return realaction;

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * move = 0; conc move = 1; collect move = 2; enemy value = - om bad, + om
	 * good
	 */
	private void TurnMoves(Hexagon A, Hexagon B, int resourcs, int move) {
		int value = 0;
		for (Hexagon neA : A.getNeighbours()) {
			if(neA == null){
				break;
			}
			if (neA.getOwner() == super.getId()) {
				Integer[] temp = new Integer[3];
				temp[0] = neA.getX();
				temp[1] = neA.getY();
				temp[2] = neA.getResources();
				goodnei.add(temp);
				continue;
			} else if (neA.getOwner() != 0) {
				if (A.getResources() - resourcs < 10 + neA.getResources()) {
					Integer[] temp = new Integer[3];
					temp[0] = neA.getX();
					temp[1] = neA.getY();
					temp[2] = neA.getResources();
					hardcorenei.add(temp);
					continue;
				} else if (A.getResources() - resourcs <= neA.getResources()) {
					Integer[] temp = new Integer[3];
					temp[0] = neA.getX();
					temp[1] = neA.getY();
					temp[2] = neA.getResources();
					badnei.add(temp);
					continue;
				} else {
					Integer[] temp = new Integer[3];
					temp[0] = neA.getX();
					temp[1] = neA.getY();
					temp[2] = neA.getResources();
					easynei.add(temp);
				}
			} else {
				Integer[] temp = new Integer[3];
				temp[0] = neA.getX();
				temp[1] = neA.getY();
				temp[2] = neA.getResources();
				nonei.add(temp);
			}
			map.put("GoodA", goodnei);
			map.put("BadA", badnei);
			map.put("HardcoreA", hardcorenei);
			map.put("NoA", nonei);
			map.put("EasyA",easynei);
		}
		// Analys

		
		if (move == 0) {
			
			value = 200-6*map.get("HardcoreA").size()-2*map.get("BadA").size()+3*map.get("GoodA").size()+8*map.get("NoA").size()+resourcs;
			if(A.getResources() > 99){
				value += 30;
			}
			
			Actionlist.add(generateMove(super.getId(),resourcs,A.getX(),A.getY(),B.getX(),B.getY(),value));
			
			
		} else if (move == 1){
			
			value = -50+3*B.getResources()+resourcs+A.getResources()-10*map.get("HardcoreA").size()-2*map.get("BadA").size()+10*map.get("GoodA").size()+4*map.get("NoA").size();
			Actionlist.add(generateMove(super.getId(),resourcs,A.getX(),A.getY(),B.getX(),B.getY(),value));
			
		} else if (move == 2){
			
			value = 10*map.get("EasyA").size()-6*map.get("HardcoreA").size()-4*map.get("BadA").size()+6*map.get("GoodA").size()+8*map.get("NoA").size()+resourcs;
			if(A.getResources() > 99){
				value += 50;
			}
			if(A.getResources()-B.getResources() - resourcs > 1){
				value +=100;
			}
			if(A.getResources()> -B.getResources()){
				value +=40;
			}
			Actionlist.add(generateMove(super.getId(),resourcs,A.getX(),A.getY(),B.getX(),B.getY(),value));
			if(A.getResources() > B.getResources()+50){
				value += 500;
				Actionlist.add(generateMove(super.getId(),resourcs,A.getX(),A.getY(),B.getX(),B.getY(),value));
			}
			
		}else if(move == 3){
			
			value = 10;
			if(A.getResources() > 99){
				value += 30;
			}
			if(B.getResources() > 99){
				value += 30;
			}
			Actionlist.add(generateMove(super.getId(),resourcs,A.getX(),A.getY(),B.getX(),B.getY(),value));
			
		}else if(move == 4){
			value = 10*map.get("NoA").size()-7*map.get("HardcoreA").size()-8*map.get("BadA").size();
			if(A.getResources() > 99){
				value += 30;
			}
			Actionlist.add(generateMove(super.getId(),resourcs,A.getX(),A.getY(),B.getX(),B.getY(),value));
		}else if(move == 5){
			if(A.getResources()>99 && map.get("GoodA").size() > 4 && B.getResources() > 99){
				value = 126;
				Actionlist.add(generateMove(super.getId(),99,A.getX(),A.getY(),B.getX(),B.getY(),value));
			}
		}else if(move == 6){
				value = 1;
				Actionlist.add(generateMove(super.getId(),A.getResources(),A.getX(),A.getY(),B.getX(),B.getY(),value));
		}else if(move == 7){
			value = 2;
			Actionlist.add(generateMove(super.getId(),A.getResources(),A.getX(),A.getY(),B.getX(),B.getY(),value));
		}else if(move == 8){
			if(A.getResources() > 100){
				
			
				if(B.getResources() < A.getResources()){
					value = 100;
					Actionlist.add(generateMove(super.getId(),A.getResources()-1,A.getX(),A.getY(),B.getX(),B.getY(),value));
				}if(B.getOwner() != super.getId()){
					value += 1000;
					Actionlist.add(generateMove(super.getId(),A.getResources()-1,A.getX(),A.getY(),B.getX(),B.getY(),value));
				}if(B.getResources() < 100+A.getResources()){
					value += 10000;
					Actionlist.add(generateMove(super.getId(),A.getResources()-1,A.getX(),A.getY(),B.getX(),B.getY(),value));
				}
			}
			
		}else if(move == 9){
			if(map.get("GoodA").size() > 6){
				
				value =100+A.getResources() - B.getResources() + resourcs;
				Actionlist.add(generateMove(super.getId(),A.getResources()-1,A.getX(),A.getY(),B.getX(),B.getY(),value));
				
			}
			
			
			
		}
		goodnei = new ArrayList<Integer[]>();
		badnei = new ArrayList<Integer[]>();
		hardcorenei = new ArrayList<Integer[]>();
		easynei = new ArrayList<Integer[]>();
		nonei = new ArrayList<Integer[]>();
	}

	private void updateHeat() {

	}

	private Integer[] generateMove(int id, int res, int x, int y, int targetx, int targety, int value) {
		Integer[] moves = new Integer[7];
		moves[0] = id;
		moves[1] = res;
		moves[2] = x;
		moves[3] = y;
		moves[4] = targetx;
		moves[5] = targety;
		moves[6] = value;
		return moves;
	}

}
