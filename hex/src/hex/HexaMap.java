package hex;

public class HexaMap {
	/**
	 * Spara hexagon i varje element
	 * 
	 * 
	 * 
	 * 
	 * 
	 **/

	public Hexagon[][] HexaMap;
	private int size;

	/**
	 * Size resulterar i sum(0,size) 6*n;
	 * 
	 * 
	 **/
	public HexaMap(int size) {
		this.size = size;
		HexaMap = new Hexagon[1 + (size - 1) * 2][1 + (size - 1) * 2];
	}

	/**
	 * Kallas vid start. S�tter ut start player & fyller Mapen Hexagon med empty
	 * hexagon samt null f�r odef
	 * 
	 * 
	 **/
	public void startMap(int PlayerAmount, Player[] player) {

		// S�tter ut hela mapen som nya hexagon
		for (int i = 0; i < HexaMap.length; i++) {
			for (int j = 0; j < HexaMap.length; j++) {
				HexaMap[i][j] = new Hexagon();
			}
		}

		// S�tter de som inte anv�nds till null
		int k = size - 1;
		for (int i = 0; i < size - 1; i++) {
			for (int j = 0; j < k; j++) {
				HexaMap[i][j] = null;
				HexaMap[HexaMap.length - i - 1][HexaMap.length - j - 1] = null;
			}
			k--;
		}
		// S�tter in player
		for (int i = 0; i < PlayerAmount; i++) {

			double angle = i * 2 * Math.PI / ((double) PlayerAmount);
			;

			float x = ((float) (((size - 1)) * ((Math.cos(Math.PI / 2 + angle)))));
			float y = ((float) (((size - 1)) * ((Math.sin(Math.PI / 2 + angle)))));

			// Test pixel to Hex
			int x2 = Math.round(((float) (Math.sqrt(3) / 3 * x - 1. / 3 * y)));
			int y2 = Math.round((float) (2. / 3 * y));
			HexaMap[size - 1 + x2][size - 1 + y2] = new Hexagon(player[i].getId(), 100);

		}

	}
	
	public void test(){
		System.out.println("test");
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
			d1 = -1; d2 =1;
			break;
		case 3:
			d1 = -1;
			break;
		case 4:
			d2 = -1;
			break;
		case 5:
			d1 = 1;d2 =-1;
			break;
		}

		if (HexaMap[x][y] == null || (x+d1 > 1+size*2) || (y+d2> 1+size*2) ){
			return null;
		}

			return HexaMap[x+d1][y+d2];
	}

}
