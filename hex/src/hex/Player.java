package hex;

import java.awt.Color;

public abstract class Player {
	protected int id;
	private Color color;
	protected Hexagon[][] myMap;

	public Player(int id, int size, Color c) {
		this.id = id;
		this.color = c;
		myMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
		for (int x = 0; x < myMap.length; x++) {
			for (int y = 0; y < myMap[x].length; y++) {
				if (x + y >= size - 1 && x + y <= size * 3 - 3) {
					myMap[x][y] = new Hexagon(x,y);
				}
			}
		}
	}
	
	public Color getColor() {
		return color;
	}

	protected void getMap(HexaMap h) {
		Hexagon[][] Aux = h.getHexaMap();
		for (int i = 0; i < Aux.length; i++) {
			for (int j = 0; j < Aux.length; j++) {
				if(Aux[i][j] != null) {
					if (Aux[i][j].getOwner() == id) {
						myMap[i][j] = Aux[i][j];
						for (int k = 0; k < 6; k++) {
							int[] temppos = h.GetNeighbourXY(i, j, k);
							myMap[temppos[0]][temppos[1]] = Aux[temppos[0]][temppos[1]];
						}
					}
				}
			}
		}

	}
	
	


	
	abstract public int[] algo(HexaMap H);

	public int getId() {
		return id;
	}
}
