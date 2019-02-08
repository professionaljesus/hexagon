package hex;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class HexaMap {
	/**
	 * Spara hexagon i varje element
	 * 
	 **/
	private Hexagon[][] HexaMap;
	private int size;

	public static final int HEXAGON_HEIGHT = 80;
	public static final int HEXAGON_WIDTH = (int) (HEXAGON_HEIGHT * Math.cos(Math.PI / 6));

	private int widht;
	private int height;
	private Stack<Integer[]> stacken;

	/**
	 * Size resulterar i sum(0,size) 6*n; HexagonMap blir size*2-1
	 * 
	 **/
	public HexaMap(int size, int width, int height) {
		this.size = size;
		this.widht = width;
		this.height = height;

		HexaMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		stacken = new Stack<Integer[]>();
	}

	/**
	 * Get's called at the endofTurn
	 * 
	 */
	public void endTurn() {
		while (!stacken.isEmpty()) {
			Integer[] t = stacken.pop();
			if (HexaMap[t[3]][t[4]].getOwner() != t[0]) {
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
			}
			if (HexaMap[t[3]][t[4]] == null || (t[3] + d1 > 1 + size * 2) || (t[4] + d2 > 1 + size * 2)) {
				continue;
			}
			if (HexaMap[t[3]][t[4]].getResources() < t[2]) {
				continue;
			}
			if (HexaMap[t[3] + d1][t[4] + d2].getOwner() == t[0]) {
				HexaMap[t[3] + d1][t[4] + d2].setResources(HexaMap[t[3] + d1][t[4] + d2].getResources() + t[2]);
				HexaMap[t[3]][t[4]].setResources(HexaMap[t[3]][t[4]].getResources() - t[2]);
			} else {
				if (HexaMap[t[3 + d1]][t[4 + d2]].getResources() > HexaMap[t[3]][t[4]].getResources()) {
					continue;
				} else {
					HexaMap[t[3 + d1]][t[4 + d2]].setResources(t[2] - HexaMap[t[3] + d1][t[4] + d2].getResources());
					HexaMap[t[3]][t[4]].setResources(HexaMap[t[3]][t[4]].getResources() - t[2]);
					HexaMap[t[3 + d1]][t[4 + d2]].setOwner(t[0]);
				}
			}
		}

		for (int i = 0; i < HexaMap.length; i++) {
			for (int j = 0; j < HexaMap.length; j++) {
				if (HexaMap[i][j] != null && HexaMap[i][j].getOwner() != 0 && HexaMap[i][j].getResources() < 100) {
					HexaMap[i][j].setResources(HexaMap[i][j].getResources() + 1);
				}
			}
		}

	}

	/**
	 * @param t
	 *            playerid, dir, res, x, y
	 *
	 */
	public void move(Integer[] t) {
		stacken.push(t);
	}

	public Hexagon[][] getHexaMap() {
		return HexaMap;
	}

	/**
	 * Kallas vid start. S�tter ut start player & fyller Mapen Hexagon med empty
	 * hexagon samt null f�r odef
	 * 
	 * 
	 **/
	public void startMap(int PlayerAmount, Player[] player) {

		// Detta borde g�ra samma
		for (int x = 0; x < HexaMap.length; x++) {
			for (int y = 0; y < HexaMap[x].length; y++) {
				if (x + y >= size - 1 && x + y <= size * 3 - 3) {
					HexaMap[x][y] = new Hexagon();
				}
			}
		}

		// Placering av spelar positioner
		switch (PlayerAmount) {
		case 3:
			HexaMap[0][size - 1].setOwner(player[0].getId());
			HexaMap[0][size - 1].setResources(100);
			HexaMap[size - 1][size * 2 - 2].setOwner(player[1].getId());
			HexaMap[size - 1][size * 2 - 2].setResources(100);
			HexaMap[size * 2 - 2][0].setOwner(player[2].getId());
			HexaMap[size * 2 - 2][0].setResources(100);
			break;
		}

	}

	/**
	 * Direction 0 = �st Direction 1 = Syd�st Direction 2 = SydV�st Direction 3
	 * = V�st Direction 4 = NorrV�st Direction 5 = Norr�st
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

	public void draw(Graphics g) {
		for (int x = 0; x < HexaMap.length; x++) {
			for (int y = 0; y < HexaMap[x].length; y++) {
				if (x + y >= size - 1 && x + y <= size * 3 - 3) {
					double originX = (widht / 2)// Center of the screen
							- HEXAGON_WIDTH * (size - 1)
							+ x * (HEXAGON_WIDTH)
							+ (y - size + 1) * (HEXAGON_WIDTH / 2);// Shift																							
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
			g.setColor(Color.BLUE);
			g.fillPolygon(px, py, 6);
			break;
		case 2:
			g.setColor(Color.RED);
			g.fillPolygon(px, py, 6);
			break;
		case 3:
			g.setColor(Color.GREEN);
			g.fillPolygon(px, py, 6);
			break;
		}

		g.setColor(Color.BLACK);
		
		g.drawString("(" + x + "," + y + ")", (int) originX - 12, (int) originY + 8);
		
	
		
		g.drawString(HexaMap[x][y].toString(), (int) originX-6, (int) originY-10);
		g.drawPolygon(px, py, 6);
	}

}
