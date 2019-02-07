package hex;

import java.awt.Graphics;
import java.util.Stack;

public class HexaMap {
	/**
	 * Spara hexagon i varje element
	 * 
	 **/
	private Hexagon[][] HexaMap;
	private int size;

	private int widht;
	private int height;
	private Stack<Integer[]> stacken;


	/**
	 * Size resulterar i sum(0,size) 6*n;
	 * HexagonMap blir 1 +(size-1)*2
	 * 
	 **/
	public HexaMap(int size, int width, int height) {
		this.size = size;
		this.widht = width;
		this.height = height;
		
		HexaMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		stacken = new Stack<Integer[]>();
	}
	
	/**Get's called at the endofTurn
	 * 
	 */
	public void endTurn() {
		while(!stacken.isEmpty()) {
			Integer[] t = stacken.pop();
			if(HexaMap[t[3]][t[4]].getOwner() != t[0]){
				continue;
			}
			int d1 = 0;
			int d2 = 0;
			switch (t[1]) {
			case 0:
				d1 = 1;
				break;
			case 1:
				d2 = 1;
				break;
			case 2:
				d1 = -1;
				d2 = 1;
				break;
			case 3:
				d1 = -1;
				break;
			case 4:
				d2 = -1;
				break;
			case 5:
				d1 = 1;
				d2 = -1;
				break;
			}if (HexaMap[t[3]][t[4]] == null || (t[3] + d1 > 1 + size * 2) || (t[4] + d2 > 1 + size * 2)) {
				continue;
			}
			if(HexaMap[t[3]][t[4]].getResources() < t[2]){
				continue;
			}
			if (HexaMap[t[3] + d1][t[4] + d2].getOwner() == t[0]){
				HexaMap[t[3] + d1][t[4] + d2].setResources(HexaMap[t[3] + d1][t[4] + d2].getResources() + t[2]);
				HexaMap[t[3]][t[4]].setResources(HexaMap[t[3]][t[4]].getResources()-t[2]);
			}else{
				if(HexaMap[t[3+ d1]][t[4 + d2]].getResources() > HexaMap[t[3]][t[4]].getResources()){
					continue;
				}else{
					HexaMap[t[3+ d1]][t[4 + d2]].setResources(t[2] - HexaMap[t[3] + d1][t[4] + d2].getResources());
					HexaMap[t[3]][t[4]].setResources(HexaMap[t[3]][t[4]].getResources()-t[2]);
					HexaMap[t[3+ d1]][t[4 + d2]].setOwner(t[0]);
				}
			}
		}
		
		for(int i=0;i<HexaMap.length;i++){
			for(int j=0;j<HexaMap.length;j++){
				if(HexaMap[i][j] !=null && HexaMap[i][j].getOwner() != 0 && HexaMap[i][j].getResources() < 100){
					HexaMap[i][j].setResources(HexaMap[i][j].getResources() + 1);
				}
			}
		}
		
	}
	
	/**
	 * @param t playerid, dir, res, x, y
	 *
	 */
	public void move(Integer[] t) {
		stacken.push(t);
	}
	
	public Hexagon[][] getHexaMap(){
		return HexaMap;
	}

	/**
	 * Kallas vid start. Sätter ut start player & fyller Mapen Hexagon med empty
	 * hexagon samt null för odef
	 * 
	 * 
	 **/
	public void startMap(int PlayerAmount, Player[] player) {

		// Sätter ut hela mapen som nya hexagon
		for (int i = 0; i < HexaMap.length; i++) {
			for (int j = 0; j < HexaMap.length; j++) {
				HexaMap[i][j] = new Hexagon();
			}
		}

		// Sätter de som inte används till null
		int k = size - 1;
		for (int i = 0; i < size - 1; i++) {
			for (int j = 0; j < k; j++) {
				HexaMap[i][j] = null;
				HexaMap[HexaMap.length - i - 1][HexaMap.length - j - 1] = null;
			}
			k--;
		}

		
		//Placering av spelar positioner
		switch(PlayerAmount){
		case 3:
			HexaMap[0][size-1].setOwner(player[0].getId());
			HexaMap[size-1][size*2-2].setOwner(player[1].getId());
			HexaMap[size*2-2][0].setOwner(player[2].getId());
			break;
		}
		

	}

	/**
	 * Direction 0 = Öst Direction 1 = SydÖst Direction 2 = SydVäst Direction 3
	 * = Väst Direction 4 = NorrVäst Direction 5 = NorrÖst
	 **/
	public Hexagon GetNeighbour(int x, int y, int Direction) {
		int d1 = 0;
		int d2 = 0;
		switch (Direction) {
		case 0:
			d1 = 1;
			break;
		case 1:
			d2 = 1;
			break;
		case 2:
			d1 = -1;
			d2 = 1;
			break;
		case 3:
			d1 = -1;
			break;
		case 4:
			d2 = -1;
			break;
		case 5:
			d1 = 1;
			d2 = -1;
			break;
		}

		if (HexaMap[x][y] == null || (x + d1 > 1 + size * 2) || (y + d2 > 1 + size * 2)) {
			return null;
		}

		return HexaMap[x + d1][y + d2];
	}
	
	public void draw(Graphics g){
		for(int x = 0; x < HexaMap.length; x++) {
			for(int y = 0; y < HexaMap[x].length; y++) {
				if(x + y >= size-1 && x+y<= size*2 +1){
					double rad = 20;
					double originX = (widht/2) - (size-0.5)*rad*Math.cos(Math.PI/6) //Center of the screen
							- rad*(size-1) *Math.cos(Math.PI/6) 
							+ x*rad*2*Math.cos(Math.PI/6) // 
							+ (y-size)*rad*Math.cos(Math.PI/6); //Shift all the hexagons
					double originY = (height/2) - (size-0.5)*rad
							- rad * (size-1) *Math.cos(Math.PI/6)
							+ y*rad*1.5;
					drawHexagon(g, x, y, originX, originY,rad);
				}
				
			}
		}
	}
	
	public void drawHexagon(Graphics g, int x, int y, double originX, double originY, double rad){
		double base = rad * Math.cos(Math.PI/6);
		int[] px,py;
		
		
		
		
		px = new int[]{
				(int) (originX - base), //top left
				(int) (originX), //top
				(int) (originX + base), // top right
				(int) (originX + base), //top left
				(int) (originX), //top
				(int) (originX - base) // top right
		};
		
		py = new int[]{
				(int) (originY - rad/2), //top left
				(int) (originY - rad), //top
				(int) (originY - rad/2), // top right
				(int) (originY + rad/2), //top left
				(int) (originY + rad), //top
				(int) (originY + rad/2 ) // top right
		};
		
		
		g.drawPolygon(px,py,6);
		g.drawString("("+x+","+y+")", (int) originX-12, (int) originY+5); 
		

	
	}
	
	

}
