package hex;

import java.awt.Color;

public class Player {
	private int id;
	private Color color;
	private Hexagon[][] myMap;

	public Player(int id, int size) {
		this.id = id;
		myMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];

		//Bajsk	kod
		for (int i = 0; i < myMap.length; i++) {
			for (int j = 0; j < myMap.length; j++) {
				myMap[i][j] = new Hexagon();
			}
		}

		int k = size - 1;
		for (int i = 0; i < size - 1; i++) {
			for (int j = 0; j < k; j++) {
				myMap[i][j] = null;
				myMap[myMap.length - i - 1][myMap.length - j - 1] = null;
			}
			k--;
		}
	}

	public Color getColor() {
		return color;
	}

	public void getMap(Hexagon[][] h) {
		for (int i = 0; i < h.length; i++) {
			for (int j = 0; j < h.length; j++) {
				if (h[i][j].getOwner() == id) {
					myMap[i][j] = h[i][j];
				}
			}
		}

	}
	
	/**
	 * Gör ett Move
	 * @param x
	 * @param y
	 * @param Direction
	 * @param resource
	 * @return
	 */
	public int[] PlayerMove(int x,int y, int Direction,int resource){
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
