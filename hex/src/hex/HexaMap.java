package hex;

import java.awt.Color;

import java.awt.Graphics;

import java.util.ArrayList;

import java.util.HashSet;

import java.util.Stack;

public class HexaMap {
	/**
	 * Spara hexagon i en HexaMap[][] Använder Axial coordinates
	 **/
	private Hexagon[][] HexaMap;
	private int size;

	public static final int HEXAGON_HEIGHT = 80;
	public static final int HEXAGON_WIDTH = (int) (HEXAGON_HEIGHT * Math.cos(Math.PI / 6));

	private Player[] player;
	private ArrayList<HashSet<Hexagon>> phex;
	private int width;
	private int height;
	private Stack<int[]> stacken;

	/**
	 * Size resulterar i sum(0,size) 6*n; HexagonMap blir size*2-1
	 * 
	 **/
	public HexaMap(int size, int width, int height, Player[] player) {
		this.size = size;
		this.width = width;
		this.height = height;
		
		phex = new ArrayList<HashSet<Hexagon>>();
		for(int i = 0; i < player.length; i++)
			phex.add(new HashSet<Hexagon>());
		
		this.player = player;

		HexaMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		stacken = new Stack<int[]>();

		// Detta borde göra samma
		for (int x = 0; x < HexaMap.length; x++) {
			for (int y = 0; y < HexaMap[x].length; y++) {
				if (x + y >= size - 1 && x + y <= size * 3 - 3) {
					HexaMap[x][y] = new Hexagon(x, y);
				}
			}
		}
		
		for(Hexagon[] uu: HexaMap) {
			for(Hexagon u: uu) {
				if(u != null) {
					Hexagon[] n = new Hexagon[6];
					for(int i = 0; i < 6; i++) {
						n[i] = getNeighbour(u.getX(), u.getY(), i);
					}
					u.setNeighbours(n);
				}
			}
		}

		// Placering av spelar positioner
		switch (player.length) {
		case 1:

			HexaMap[0][size - 1].setOwner(player[0].getId());
			HexaMap[0][size - 1].setResources(100);
			phex.get(0).add(HexaMap[0][size - 1]);
			break;
		case 3:
			HexaMap[0][size - 1].setOwner(player[0].getId());
			HexaMap[0][size - 1].setResources(100);
			phex.get(0).add(HexaMap[0][size - 1]);

			HexaMap[size - 1][size * 2 - 2].setOwner(player[1].getId());
			HexaMap[size - 1][size * 2 - 2].setResources(100);
			phex.get(1).add(HexaMap[size - 1][size * 2 - 2]);

			HexaMap[size * 2 - 2][0].setOwner(player[2].getId());
			HexaMap[size * 2 - 2][0].setResources(100);
			phex.get(2).add(HexaMap[size * 2 - 2][0]);

			break;
		}
	}

	public ArrayList<HashSet<Hexagon>> getPhex() {
		return phex;
	}

	/**
	 * Get's called at the endofTurn t[0] user t[1] resource 
	 * t[2] x
	 *  t[3] y t[4] targetX t[5] targetY
	 * 
	 * Om dir > 5 så går den på targetX och Y
	 */
	public void endTurn() {
		while (!stacken.isEmpty()) {
			int[] t = stacken.pop();
			
			int id = t[0];
			int res = t[1];
			int x = t[2];
			int y = t[3];
			int targetX = t[4];
			int targetY = t[5];
			
			
			if (HexaMap[x][y].getOwner() != id) {
				continue;

			}


			// Om man har för lite resources
			if (HexaMap[x][y].getResources() < res)
				continue;

			if (HexaMap[targetX][targetY].getOwner() == id) {
				HexaMap[targetX][targetY].setResources(HexaMap[targetX][targetY].getResources() + res);
				HexaMap[x][y].setResources(HexaMap[x][y].getResources() - res);
				if (HexaMap[x][y].getResources() == 0) {
					HexaMap[x][y].setOwner(0);
					phex.get(id - 1).remove(HexaMap[x][y]);
				}
			} else { // Någon annans ruta
				if (HexaMap[targetX][targetY].getResources() > HexaMap[x][y].getResources()) {
					continue;
				} else { // Mer resources än den
					HexaMap[targetX][targetY].setResources(res - HexaMap[targetX][targetY].getResources());
					HexaMap[x][y].setResources(HexaMap[x][y].getResources() - res);

					// Du slösa alla
					if (HexaMap[x][y].getResources() == 0) {
						HexaMap[x][y].setOwner(0);
						phex.get(id - 1).remove(HexaMap[x][y]);
					}

						for(HashSet<Hexagon> a : phex){
							a.remove(HexaMap[targetX][targetY]);
						}
						phex.get(t[0] - 1).add(HexaMap[targetX][targetY]);
						HexaMap[targetX][targetY].setOwner(id);
					
				//	HexaMap[t[3 + targetX]][t[4 + targetY]].setColor

				}
			}
		}



		for(HashSet<Hexagon> uu: phex) {
			for(Hexagon u: uu) {
				if(u.getResources() < 100)
					u.setResources(u.getResources() + 1);
			}
		}

			
	



	}

	/**
	 * @param t
	 *            playerid, dir, res, x, y
	 *
	 */
	public void move(int[] t) {

		stacken.push(t);
	}

	public Hexagon[][] getHexaMap() {
		return HexaMap;
	}

	public Hexagon getNeighbour(int x, int y, int direction) {
		int[] target = getNeighbourXY(x,y,direction);
		return HexaMap[target[0]][target[1]];
	}

	public int[] getNeighbourXY(int x, int y, int Direction) {
		int targetX = 0;
		int targetY = 0;
		int[] pos = new int[2];
		switch (Direction) {
		case 0: // Höger
			if (x + y == size * 3 - 3) { // bottom right
				targetX = x - (size - 1);
				targetY = y - (size - 1);
			} else if (x == size * 2 - 2) { // top right
				if (y < size - 1) {
					targetX = 0;
					targetY = y + size;
				} else {
					targetX = size - 1;
					targetY = 0;
				}
			} else {
				targetX = x + 1;
				targetY = y;
			}
			break;
		case 1: // Neråt Höger
			if (y == size * 2 - 2) { // bottom
				targetX = x + (size - 1);
				targetY = 0;
			} else if (x + y == size * 3 - 3) { // bottom right
				targetX = x - size;
				targetY = y - (size - 2);
			} else {
				targetX = x;
				targetY = y + 1;
			}
			break;
		case 2: // Neråt Vänster
			if (x == 0) { // bottom left
				targetX = size * 2 - 2;
				targetY = y - (size - 1);
			} else if (y == size * 2 - 2) { // bottom
				targetX = x + (size / 2);
				targetY = 0;
			} else {
				targetX = x - 1;
				targetY = y + 1;
			}
			break;
		case 3: // Vänster
			if (x + y == size - 1) { // top left
				targetX = x + (size - 1);
				targetY = y + (size - 1);
			} else if (x == 0) { // bottom left
				targetX = size * 2 - 2;
				targetY = y - size;
			} else {
				targetX = x - 1;
				targetY = y;
			}
			break;
		case 4: // Upp åt vänster
			if (y == 0) {// top
				targetX = x - (size - 1);
				targetY = size * 2 - 2;
			} else if (x + y == size - 1) {// top left
				targetX = x + size;
				targetY = y + (size - 2);
			} else {
				targetX = x;
				targetY = y - 1;
			}
			break;
		case 5: // Upp åt Höger
			if (x == size * 2 - 2) {// top right
				targetX = 0;
				targetY = y + (size - 1);
			} else if (y == 0) {// top
				targetX = x - (size - 2);
				targetY = size * 2 - 2;
			} else {
				targetX = x + 1;
				targetY = y - 1;
			}
			break;
		}
		pos[0] = targetX;
		pos[1] = targetY;
		return pos;
	}

	public void draw(Graphics g) {
		for (int x = 0; x < HexaMap.length; x++) {
			for (int y = 0; y < HexaMap[x].length; y++) {
				if (x + y >= size - 1 && x + y <= size * 3 - 3) {
					double originX = (width / 2)// Center of the screen
							- HEXAGON_WIDTH * (size - 1) + x * (HEXAGON_WIDTH) + (y - size + 1) * (HEXAGON_WIDTH / 2);// Shift
					double originY = (height / 2) - HEXAGON_HEIGHT * (size - 1) * 0.75 + y * (HEXAGON_HEIGHT / 2) * 1.5;
					drawHexagon(g, x, y, originX, originY);
				}

			}
		}
	}

	public void drawHexagon(Graphics g, int x, int y, double originX, double originY) {

		int[] px, py;

		px = new int[] { (int) (originX - (HEXAGON_WIDTH / 2)), // top left
				(int) (originX), // top
				(int) (originX + (HEXAGON_WIDTH / 2)), // top right
				(int) (originX + (HEXAGON_WIDTH / 2)), // top left
				(int) (originX), // top
				(int) (originX - (HEXAGON_WIDTH / 2)) // top right
		};

		py = new int[] { (int) (originY - (HEXAGON_HEIGHT / 4)), // top left
				(int) (originY - (HEXAGON_HEIGHT / 2)), // top
				(int) (originY - (HEXAGON_HEIGHT / 4)), // top right
				(int) (originY + (HEXAGON_HEIGHT / 4)), // top left
				(int) (originY + (HEXAGON_HEIGHT / 2)), // top
				(int) (originY + (HEXAGON_HEIGHT / 4)) // top right
		};

		switch (HexaMap[x][y].getOwner()) {

		case 1:
			g.setColor(player[0].getColor());
			g.fillPolygon(px, py, 6);
			break;
		case 2:
			g.setColor(player[1].getColor());
			g.fillPolygon(px, py, 6);
			break;
		case 3:
			g.setColor(player[2].getColor());
			g.fillPolygon(px, py, 6);
			break;
		}

		g.setColor(Color.BLACK);

		g.drawString("(" + x + "," + y + ")", (int) originX - 12, (int) originY + 8);

		g.drawString(HexaMap[x][y].toString(),
				(int) originX - g.getFontMetrics().stringWidth(HexaMap[x][y].toString()) / 2, (int) originY - 10);

		g.drawPolygon(px, py, 6);
	}

}
