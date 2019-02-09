package hex;

import java.awt.Color;

public class Player {
	private int id;
	private Color color;
	private Hexagon[][] myMap;

	public Player(int id, int size) {
		this.id = id;
		myMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		for (int x = 0; x < myMap.length; x++) {
			for (int y = 0; y < myMap[x].length; y++) {
				if (x + y >= size - 1 && x + y <= size * 3 - 3) {
					myMap[x][y] = new Hexagon();
				}
			}
		}
	}

	public Color getColor() {
		return color;
	}

	public void getMap(HexaMap h) {
		Hexagon[][] Aux = h.getHexaMap();
		for (int i = 0; i < Aux.length; i++) {
			for (int j = 0; j < Aux.length; j++) {
				if (Aux[i][j].getOwner() == id) {
					myMap[i][j] = Aux[i][j];
					for (int k = 0; k < 6; k++) {
						int[] temppos = h.GetNeighbour2(i, j, k);
						myMap[temppos[0]][temppos[1]] = Aux[temppos[0]][temppos[1]];
					}
				}
			}
		}

	}

	/**
	 * Gör ett Move
	 * 
	 * @param x
	 * @param y
	 * @param Direction
	 * @param resource
	 * @return
	 */
	public int[] PlayerMove(int x, int y, int Direction, int resource) {
		int[] v = new int[5];
		v[0] = this.id;
		v[1] = Direction;
		v[2] = resource;
		v[3] = x;
		v[4] = y;
		return v;

	}

	public int getId() {
		return id;
	}
}
