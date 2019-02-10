package hex;
import java.awt.Color;
import java.util.HashSet;
public class SimpleBot extends Player {
	int turnorder;
	int own;
	int generating;
	int turn;
	private Hexagon[][] heatMap;
	private int[] heatdir;
	private Hexagon[][] myMap;
	private HashSet<int[]> Actionlist;

	public SimpleBot(int id, int size,Color color) {
		super(id, size,color);
		myMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		heatMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
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
		//Updata data
		generating = 0;
		own = H.size();
		for(Hexagon h : H){
			if(h.getResources() < 100){
				generating++;
			}
			myMap[h.getX()][h.getY()] = h;
			for(Hexagon ne : h.getNeighbours()){
				myMap[ne.getX()][ne.getY()] = ne;
				if(ne.getOwner() == super.getId()){
					TurnMoves(h, ne, h.getResources(),1);
				}else if (ne.getOwner() == 0){
					TurnMoves(h, ne, h.getResources(),0);
				}else{
					TurnMoves(h, ne, h.getResources(),2);
				}
				
				
			}
			
			
			
			
			
		}
		
		
		
		
		
		
		
//		super.getMap(H);
//		for (int x = 0; x < super.myMap.length; x++) {
//			for (int y = 0; y < super.myMap.length; y++) {
//				if (super.myMap[x][y].getOwner() == super.getId()) {
//					own++;
//					if (super.myMap[x][y].getResources() < 100) {
//						generating++;
//					}
//				}
//				if (super.myMap[x][y].getOwner() != super.getId() && super.myMap[x][y].getOwner() != 0) {
//					heatMap[x][y].setOwner(super.myMap[x][y].getOwner());
//					if (heatMap[x][y].getResources() != super.myMap[x][y].getResources()) {
//						heatMap[x][y].setResources(super.myMap[x][y].getResources());
//					}
//				}
//			}
//		}
		turn++;
		return null;
	}
	
	/**
	 * move = 0; conc
	 * move = 1; collect
	 * move = 2; enemy
	 * value = - om bad, + om good
	 */
	private void TurnMoves(Hexagon A, Hexagon B, int resourcs,int move){
		int value = 0;
		int GoodNeibooursA =0;
		int EvilNeibooursA = 0;
		int HardcoreNeibooursA =0;
		int GoodNeibooursB =0;
		int EvilNeibooursB = 0;
		int HardcoreNeibooursB =0;
		for(Hexagon neA : A.getNeighbours()){
			if(neA.getOwner() == super.getId()){
				GoodNeibooursA++;
				continue;
			}else{
				if(neA.getOwner() != super.getId() && A.getResources() < 10+neA.getResources()){
					HardcoreNeibooursA++;
					continue;
				}else if(neA.getOwner() != super.getId() && A.getResources() > neA.getResources()){
					EvilNeibooursA++;
					continue;
				}
			}
		}
		for(Hexagon neB : B.getNeighbours()){
			if(neB.getOwner() == super.getId()){
				GoodNeibooursB++;
				continue;
			}else{
				if(neB.getOwner() != super.getId() && A.getResources() < 10+neB.getResources()){
					HardcoreNeibooursB++;
					continue;
				}else if(neB.getOwner() != super.getId() && A.getResources() > neB.getResources()){
					EvilNeibooursB++;
					continue;
				}
			}
		}
		
		if(move == 1){

		}else if(move == 0){
			value += resource*5*(6-) 
			
			
			
		}else{
			
		}
		
		
		
	}
	
	
	private void updateHeat(){
		
		
		
	}
	
	


}
