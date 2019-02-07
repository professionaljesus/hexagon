package hex;

public class Player {
	private int id;

	private Hexagon[][] myMap;

	public Player(int id, int size) {
		this.id = id;
		myMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];

		
		//Bajskod
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

	public void getMap(Hexagon[][] h) {
		for (int i = 0; i < h.length; i++) {
			for (int j = 0; j < h.length; j++) {
				if (h[i][j].getOwner() == id) {
					myMap[i][j] = h[i][j];
				}
			}
		}

	}

	public int getId() {
		return id;
	}
}
